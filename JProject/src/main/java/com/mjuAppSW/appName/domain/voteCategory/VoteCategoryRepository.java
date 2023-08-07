package com.mjuAppSW.appName.domain.voteCategory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCategoryRepository extends JpaRepository<VoteCategory, Long> {
}
