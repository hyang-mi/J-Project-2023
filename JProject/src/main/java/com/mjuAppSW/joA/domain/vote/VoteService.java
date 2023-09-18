package com.mjuAppSW.joA.domain.vote;

import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.domain.report.ReportCategory;
import com.mjuAppSW.joA.domain.report.ReportCategoryRepository;
import com.mjuAppSW.joA.domain.report.vote.VoteReport;
import com.mjuAppSW.joA.domain.report.vote.VoteReportRepository;
import com.mjuAppSW.joA.domain.vote.dto.*;
import com.mjuAppSW.joA.domain.voteCategory.VoteCategory;
import com.mjuAppSW.joA.domain.voteCategory.VoteCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteCategoryRepository voteCategoryRepository;
    private final VoteReportRepository voteReportRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StatusResponse sendVote(SendRequest request) {
        Member giveMember = findByMemberId(request.getGiveId());
        Member takeMember = findByMemberId(request.getTakeId());
        VoteCategory voteCategory = voteCategoryRepository.findById(request.getCategoryId()).orElse(null);
        if(giveMember == null || takeMember == null)
            return new StatusResponse(3);

        if(voteCategory == null)
            return new StatusResponse(2);

        Optional<Vote> equalVote = voteRepository.findEqualVote(request.getGiveId(), request.getTakeId(),
                                                                request.getCategoryId(), LocalDate.now());
        if (equalVote.isPresent())
            return new StatusResponse(1);

        Vote vote = Vote.builder().giveId(request.getGiveId())
                                .member(takeMember)
                                .voteCategory(voteCategory)
                                .date(LocalDate.now())
                                .hint(request.getHint()).build();
        voteRepository.save(vote);
        return new StatusResponse(0);
    }

    public VoteListResponse getVotes(Long takeId) {
        Member findTakeMember = findByMemberId(takeId);
        if(findTakeMember == null) return null;

        Pageable pageable = PageRequest.of(0, 30);
        List<Vote> votes = voteRepository.findAllByTakeId(takeId, pageable);
        List<VoteContent> voteList = new ArrayList<>();
        for (Vote vote : votes) {
            VoteContent voteContent = VoteContent.builder().voteId(vote.getId())
                                                        .categoryId(vote.getVoteCategory().getId())
                                                        .hint(vote.getHint()).build();
            voteList.add(voteContent);
        }
        return new VoteListResponse(voteList);
    }

    @Transactional
    public StatusResponse reportVote(ReportRequest request) {
        Vote vote = voteRepository.findById(request.getVoteId()).orElse(null);
        ReportCategory category = reportCategoryRepository.findById(request.getReportId()).orElse(null);
        if (vote == null)
            return new StatusResponse(2);
        if (category == null)
            return new StatusResponse(3);

        Optional<VoteReport> findReport = voteReportRepository.findByVoteId(request.getVoteId());
        if(findReport.isPresent())
            return new StatusResponse(1);

        VoteReport voteReport = new VoteReport(vote, category, request.getContent(), LocalDateTime.now());
        voteReportRepository.save(voteReport);
        return new StatusResponse(0);
    }

    private Member findByMemberId(Long id) {
        Member member =  memberRepository.findById(id).orElse(null);
        if(member == null || member.getWithdrawal() == true) return null;
        return member;
    }
}
