package com.mjuAppSW.appName.domain.heart;

import com.mjuAppSW.appName.domain.heart.dto.HeartRequest;
import com.mjuAppSW.appName.domain.heart.dto.HeartResponse;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;

    public HeartResponse sendHeart(Long giveId, Long takeId, Boolean named) {
        Optional<Heart> equalHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), giveId, takeId);
        if (equalHeart.isPresent()) {
            return null; // 이미 하트를 누른 상태
        }
        Optional<Heart> opponentHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), takeId, giveId);
        Optional<Member> giveMember = memberRepository.findById(giveId);
        Optional<Member> takeMember = memberRepository.findById(takeId);

        if (giveMember.isEmpty() || takeMember.isEmpty()) {
            return null; // id가 유효하지 않음
        }

        Heart heart = new Heart(giveId, takeMember.get(), LocalDate.now(), named);
        heartRepository.save(heart);

        if(opponentHeart.isPresent()) {
            String giveImage = findImage(giveMember.get());
            String takeImage = findImage(takeMember.get());
            return new HeartResponse(true, giveMember.get().getName(), giveImage, takeImage);
        }
        else {
            if (named) {
                return new HeartResponse(false, giveMember.get().getName(), null, null);
            }
            else {
                return new HeartResponse(false, null, null, null);
            }
        }
    }

    private String findImage(Member member) {
        String imagePath = member.getImagePath();
        if(imagePath == null) // 저장소에 이미지 경로 미리 있느냐 / 없느냐에 따라 구현 달라질 듯
            return null;
        return encodeImage(imagePath);
    }

    private String encodeImage(String imagePath) {
        String filePath = "storagePath" + imagePath; // 파일 저장소 경로
        File imageFile = new File(filePath);
        try {
            // imageFile이 null이라면?
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            log.info("프로필 사진 경로가 유효하지 않음");
            return null;
        }
    }
}
