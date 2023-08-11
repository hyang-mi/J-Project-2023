package com.mjuAppSW.appName.domain.vote;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.domain.report.ReportCategory;
import com.mjuAppSW.appName.domain.report.ReportCategoryRepository;
import com.mjuAppSW.appName.domain.report.VoteReport;
import com.mjuAppSW.appName.domain.report.VoteReportRepository;
import com.mjuAppSW.appName.domain.vote.dto.*;
import com.mjuAppSW.appName.domain.voteCategory.VoteCategory;
import com.mjuAppSW.appName.domain.voteCategory.VoteCategoryRepository;
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
    public VoteResponse sendVote(SendRequest request) {
        Member giveMember = memberRepository.findById(request.getGiveId()).orElse(null);
        Member takeMember = memberRepository.findById(request.getTakeId()).orElse(null);
        VoteCategory voteCategory = voteCategoryRepository.findById(request.getCategoryId()).orElse(null);
        if(giveMember == null || takeMember == null || voteCategory == null)
            return new VoteResponse(2);

        Optional<Vote> equalVote = voteRepository.findEqualVote(request.getGiveId(), request.getTakeId(), request.getCategoryId(), LocalDate.now());
        if (equalVote.isPresent())
            return new VoteResponse(1);

        Vote vote = Vote.builder().giveId(request.getGiveId())
                                    .member(takeMember)
                                    .voteCategory(voteCategory)
                                    .date(LocalDate.now())
                                    .hint(request.getHint()).build();
        voteRepository.save(vote);
        return new VoteResponse(0);
    }

    public VoteListResponse getVotes(Long takeId) {
        Optional<Member> findTakeMember = memberRepository.findById(takeId);
        if(findTakeMember.isEmpty()) return null;

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
    public VoteResponse reportVote(ReportRequest request) {
        Vote vote = voteRepository.findById(request.getVoteId()).orElse(null);
        ReportCategory category = reportCategoryRepository.findById(request.getReportId()).orElse(null);
        if (vote == null || category == null)
            return new VoteResponse(2);

        Optional<VoteReport> findReport = voteReportRepository.findByVoteId(request.getVoteId());
        if(findReport.isPresent())
            return new VoteResponse(1);

        VoteReport voteReport = new VoteReport(vote, category, request.getContent(), LocalDateTime.now());
        voteReportRepository.save(voteReport);
        return new VoteResponse(0);
    }
}
