package com.mjuAppSW.joA.geography;

import com.mjuAppSW.joA.geography.dto.*;
import com.mjuAppSW.joA.domain.member.Member;
import com.mjuAppSW.joA.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeoService {

    private final GeoRepository geoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StatusResponse updateLocation(UpdateRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Location location = geoRepository.findById(request.getId()).orElse(null);
        if(location == null) return new StatusResponse(1); // 존재하지 않는 멤버의 위치

        Location newLocation = new Location(location.getId(), location.getCollegeId(), point);
        geoRepository.save(newLocation);
        return new StatusResponse(0); // 정상 업데이트
    }

    public NearByListResponse getNearByList(UpdateRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Member member = findByMemberId(request.getId());
        if(member == null) return null;

        List<Long> nearIds = geoRepository.findNearIds(request.getId(), point, member.getCollege().getId());
        List<NearByInfo> nearByInfoList = new ArrayList<>();

        for (Long nearId : nearIds) {
            Member findMember = findByMemberId(nearId);
            String urlCode = null;
            if(!findMember.getBasicProfile())
                urlCode = findMember.getUrlCode();

            nearByInfoList.add(NearByInfo.builder().name(findMember.getName())
                                                .urlCode(urlCode)
                                                .bio(findMember.getBio()).build());
        }
        return new NearByListResponse(nearByInfoList);
    }

    @Transactional
    public void deleteLocation(IdRequest request) {
        geoRepository.deleteById(request.getId());
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
