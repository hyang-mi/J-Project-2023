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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public Integer sendVote(SendRequest request) {
        Optional<Member> findGiveMember = memberRepository.findById(request.getGiveId());
        Optional<Member> findTakeMember = memberRepository.findById(request.getTakeId());
        Optional<VoteCategory> findCategory = voteCategoryRepository.findById(request.getCategoryId());
        if(findGiveMember.isEmpty() || findTakeMember.isEmpty() || findCategory.isEmpty())
            return 2; // 존재하지 않는 Id, 유효하지 않은 접근

        Optional<Vote> equalVote = voteRepository.findEqualVote(request.getGiveId(), request.getTakeId(), request.getCategoryId(), LocalDate.now());
        if (equalVote.isPresent())
            return 1; // 이미 투표를 한 상태

        Vote vote = new Vote(request.getGiveId(), findTakeMember.get(), findCategory.get(), LocalDate.now(), request.getHint());
        voteRepository.save(vote);
        return 0; // 투표 성공
    }

    public VoteListResponse getVotes(OwnerRequest request) {
        Optional<Member> findTakeMember = memberRepository.findById(request.getTakeId());
        if(findTakeMember.isEmpty())
            return null;

        Pageable pageable = PageRequest.of(0, 30);
        List<Vote> votes = voteRepository.findAllByTakeId(request.getTakeId(), pageable); // N+1 문제?
        List<VoteContent> voteList = new ArrayList<>();
        for (Vote vote : votes) {
            VoteContent voteContent = new VoteContent(vote.getId(), vote.getVoteCategory().getId(), vote.getHint());
            voteList.add(voteContent);
        }
        return new VoteListResponse(voteList);
    }

    public Integer reportVote(ReportRequest request) {
        Optional<Vote> findVote = voteRepository.findById(request.getVoteId());
        Optional<ReportCategory> findCategory = reportCategoryRepository.findById(request.getCategoryId());
        if (findVote.isEmpty() || findCategory.isEmpty())
            return 2; // id 존재하지 않음, 유효하지 않은 접근

        Optional<VoteReport> findReport = voteReportRepository.findByVoteId(request.getVoteId());
        if(findReport.isPresent())
            return 1; // 이미 해당 투표에 대한 신고가 존재함

        VoteReport voteReport = new VoteReport(findVote.get(), findCategory.get(), request.getContent(), LocalDateTime.now());
        voteReportRepository.save(voteReport);
        return 0; // 신고 저장 성공
    }
}
