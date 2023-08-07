package com.mjuAppSW.appName.domain.geography;

import com.mjuAppSW.appName.domain.geography.dto.NearByInfo;
import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.domain.geography.dto.LocationRequest;
import com.mjuAppSW.appName.domain.geography.dto.NearByListResponse;
import com.mjuAppSW.appName.domain.geography.dto.OwnerRequest;
import com.mjuAppSW.appName.storage.S3Uploader;
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
    private final S3Uploader s3Uploader;

    public void updateLocation(LocationRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        Location location = new Location(request.getId(), point);
        geoRepository.save(location);
    }

    public NearByListResponse getNearByList(LocationRequest request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude(), request.getAltitude());
        List<Long> nearIds = geoRepository.findNearById(request.getId(), point);

        List<NearByInfo> nearByInfos = new ArrayList<>();

        for (Long nearId : nearIds) {
            Member member = memberRepository.findById(nearId).orElseThrow();
            String base64Picture = null;
            if(!member.getBasicProfile())
                base64Picture = s3Uploader.getPicture(String.valueOf(member.getId()));
            nearByInfos.add(new NearByInfo(member.getName(), base64Picture, member.getBio()));
        }

        NearByListResponse nearByList = new NearByListResponse();
        nearByList.setNearByList(nearByInfos);

        return nearByList;
    }

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
