package com.mjuAppSW.appName.domain.member.geography;

import com.mjuAppSW.appName.domain.member.Member;
import com.mjuAppSW.appName.domain.member.MemberRepository;
import com.mjuAppSW.appName.domain.member.geography.dto.MemberLocation;
import com.mjuAppSW.appName.domain.member.geography.dto.NearByInfo;
import com.mjuAppSW.appName.domain.member.picture.RedisUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class GeoService {

    private final GeoRepository geoRepository;
    private final MemberRepository memberRepository;
    private final RedisUploader redisUploader;

    public void updateLocation(MemberLocation request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude());
        geoRepository.save(new Location(request.getId(), point));
    }

    public List<NearByInfo> getNearByList(MemberLocation request) {
        Point point = getPoint(request.getLatitude(), request.getLongitude());
        List<Long> nearByIds = geoRepository.findNearByIds(request.getId(), point);

        List<NearByInfo> nearByList = new ArrayList<>();

        for (Long id : nearByIds) {
            Optional<Member> findMember = memberRepository.findById(id);
            if(findMember.isEmpty()) continue; // 다른 예외 처리 필요할 듯

            Member member = findMember.get();
            String base64Picture = null;
            if(!member.getBasicProfile())
                base64Picture = redisUploader.bringPicture(member.getId());
            nearByList.add(new NearByInfo(member.getName(), base64Picture, member.getBio()));
        }

        return nearByList;
    }

    private Point getPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(latitude, longitude));
        return point;
    }
}
