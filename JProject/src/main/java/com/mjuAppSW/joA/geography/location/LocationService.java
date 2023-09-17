package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.domain.heart.Heart;
import com.mjuAppSW.joA.domain.heart.HeartRepository;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import com.mjuAppSW.joA.geography.college.PCollege;
import com.mjuAppSW.joA.geography.college.PCollegeRepository;
import com.mjuAppSW.joA.geography.location.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final PCollegeRepository pCollegeRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public UpdateResponse updateLocation(UpdateRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Location location = locationRepository.findById(request.getId()).orElse(null);
        if(location == null) return new UpdateResponse(2, null); // 존재하지 않는 멤버

        PCollege college = pCollegeRepository.findById(location.getCollege().getCollegeId()).orElse(null);
        if(college == null) return new UpdateResponse(1, null); // 존재하지 않는 학교

        boolean isContained = isPointWithinPolygon(request.getLatitude(), request.getLongitude(), college.getPolygonField());

        Location newLocation = new Location(location.getId(), location.getCollege(), point, isContained);
        locationRepository.save(newLocation);
        return new UpdateResponse(0, isContained); // 정상 업데이트
    }

    public NearByListResponse getNearByList(UpdateRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Member member = findByMemberId(request.getId());
        if(member == null) return new NearByListResponse(3); // 사용자 존재 X

        Location location = locationRepository.findById(request.getId()).orElse(null);
        if(location == null) return new NearByListResponse(2); // 사용자 위치 존재 X

        if(!location.getIsContained()) return new NearByListResponse(1); // 사용자 학교 밖

        List<Long> nearIds = locationRepository.findNearIds(request.getId(), point, member.getCollege().getId());
        List<NearByInfo> nearByInfoList = new ArrayList<>();

        for (Long nearId : nearIds) {
            Member findMember = findByMemberId(nearId);
            Heart findEqualHeart = heartRepository.findEqualHeartByIdAndDate(LocalDate.now(), request.getId(), nearId).orElse(null);

            String urlCode = "";
            Boolean isLiked = false;

            if(!findMember.getBasicProfile())
                urlCode = findMember.getUrlCode();
            if(findEqualHeart != null)
                isLiked = true;

            nearByInfoList.add(NearByInfo.builder().name(findMember.getName())
                                                .urlCode(urlCode)
                                                .bio(findMember.getBio())
                                                .isLiked(isLiked).build());
        }
        return new NearByListResponse(0, nearByInfoList);
    }

    @Transactional
    public void setPolygon(PolygonRequest request) {
        Polygon polygon = makePolygon(request);

        PCollege college = new PCollege(request.getCollegeId(), polygon);
        pCollegeRepository.save(college);
    }

//    @Transactional
//    public void deleteLocation(IdRequest request) {
//        locationRepository.deleteById(request.getId());
//    }

    private Polygon makePolygon(PolygonRequest request) {
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] coordinates = new Coordinate[] {
                new Coordinate(request.getTopLeftLng(), request.getTopLeftLat()),
                new Coordinate(request.getTopRightLng(), request.getTopRightLat()),
                new Coordinate(request.getBottomRightLng(), request.getBottomRightLat()),
                new Coordinate(request.getBottomLeftLng(), request.getBottomLeftLat()),
                new Coordinate(request.getTopLeftLng(), request.getTopLeftLat())
        };

        return geometryFactory.createPolygon(coordinates);
    }

    private boolean isPointWithinPolygon(double latitude, double longitude, Polygon polygon) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        return polygon.contains(point);
    }

    private Point getPoint(double latitude, double longitude, double altitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude, altitude);
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }

    private Member findByMemberId(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

}
