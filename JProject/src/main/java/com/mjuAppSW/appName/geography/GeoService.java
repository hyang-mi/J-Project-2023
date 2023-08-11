package com.mjuAppSW.appName.geography;

import com.mjuAppSW.appName.geography.dto.NearByInfo;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.geography.dto.LocationRequest;
import com.mjuAppSW.appName.geography.dto.NearByListResponse;
import com.mjuAppSW.appName.geography.dto.OwnerRequest;
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
    public void updateLocation(LocationRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        log.info("point = {}", point);
        Location location = new Location(request.getId(), point);
        geoRepository.save(location);
    }

    public NearByListResponse getNearByList(LocationRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Member member = memberRepository.findById(request.getId()).orElse(null);
        //
        List<Long> nearIds = geoRepository.findNearIds(request.getId(), point, member.getCollege().getId());
        List<NearByInfo> nearByInfos = new ArrayList<>();

        for (Long nearId : nearIds) {
            Member findMember = memberRepository.findById(nearId).orElse(null);
            if(findMember == null) continue; //
            String urlCode = null;
            if(!findMember.getBasicProfile())
                urlCode = findMember.getUrlCode();

            nearByInfos.add(NearByInfo.builder().name(findMember.getName())
                                                .urlCode(urlCode)
                                                .bio(findMember.getBio()).build());
        }
        return new NearByListResponse(nearByInfos);
    }

    @Transactional
    public void deleteLocation(OwnerRequest request) {
        geoRepository.deleteById(request.getId());
    }

    private Point getPoint(double latitude, double longitude, double altitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude, altitude);
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }
}
