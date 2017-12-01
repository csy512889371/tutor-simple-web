package com.rjsoft.uums.core.position.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.position.entity.UmsUserPositionRelation;
import com.rjsoft.uums.facade.user.entity.UmsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsUserPositionRelationRepository extends CustomRepository<UmsUserPositionRelation, String> {

    @Query("select a.position.id from UmsUserPositionRelation a,UmsPosition b where a.position.id=b.id and a.user.id=?1 and b.isAvailable=?2")
    List<String> findPositionIds(String userId, Short isAvailable);

    UmsUserPositionRelation findByPositionIdAndUserId(String positionId, String userId);

    @Query("select a.user.id from UmsUserPositionRelation a where a.position.id=?1")
    List<String> findUserIdByPositionId(String positionId);

    @Query("select b from UmsUserPositionRelation a,UmsUser b where a.user.id = b.id and a.position.id=?1 and b.nickname like %?2% and b.username like %?3% and b.isAvailable='1' order by b.createDate DESC")
    Page<UmsUser> findUserByPositionId(String positionId, String nickname, String username, Pageable page);

    //    @Query("select b from UmsUserPositionRelation a,UmsUser b where a.userId != b.id and a.positionId=?1 and b.nickname like %?2% and b.username like %?3% order by b.createDate DESC")
    @Query("select u from UmsUser u where not exists (select a.user.id from UmsUserPositionRelation a where a.user.id = u.id and a.position.id=?1)  and u.nickname like %?2% and u.username like %?3% and u.isAvailable='1' order by u.createDate DESC")
    Page<UmsUser> findNotUserByPositionId(String positionId, String nickname, String username, Pageable page);

    @Query("select b from UmsUserPositionRelation a,UmsUser b,UmsPerson c where a.user.id = b.id and b.person.id = c.id and a.position.id=?1 and c.name like %?2% and c.sn like %?3% and b.isAvailable='1' order by b.createDate DESC")
    Page<UmsUser> findUserByPositionIdAndNameAndSn(String positionId, String name, String sn, Pageable page);

    //    @Query("select b from UmsUserPositionRelation a,UmsUser b where a.userId != b.id and a.positionId=?1 and b.nickname like %?2% and b.username like %?3% order by b.createDate DESC")
    @Query("select u from UmsUser u, UmsPerson p where not exists (select a.user.id from UmsUserPositionRelation a where a.user.id = u.id and a.position.id=?1) and u.person.id=p.id and p.name like %?2% and p.sn like %?3% and u.isAvailable='1' order by u.createDate DESC")
    Page<UmsUser> findNotUserByPositionIdAndNameAndSn(String positionId, String name, String sn, Pageable page);

    @Modifying
    @Query("delete from UmsUserPositionRelation where position.id=?1")
    public void clearUserPositionRelation(String positionId);

    @Modifying
    @Query("delete from UmsUserPositionRelation where user.id=?1")
    public void clearUserPositionRelationByUserId(String userId);

    @Modifying
    @Query("delete from UmsUserPositionRelation where position.id=?1 and user.id in ?2")
    public void clearUserPositionRelation(String positionId, List<String> userIds);

    @Modifying
    @Query("delete from UmsUserPositionRelation where position.id=?1 and user.id=?2")
    public void deleteUserPositionRelation(String positionId, String userId);
}
