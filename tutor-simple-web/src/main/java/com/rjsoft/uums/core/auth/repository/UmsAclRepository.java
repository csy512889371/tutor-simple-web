package com.rjsoft.uums.core.auth.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.auth.entity.UmsAcl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UmsAclRepository extends CustomRepository<UmsAcl, String> {

    @Query("select o from UmsAcl o where o.pid=?1 and o.ptype=?2 and o.rid=?3 and o.rtype=?4")
    public UmsAcl getAcl(String pid, String ptype, String rid, String rtype);

    @Query("select o from UmsAcl o where o.pid=?1 and o.ptype=?2 and o.rtype=?3")
    public List<UmsAcl> getAcl(String pid, String ptype, String rtype);

    @Query("select o from UmsAcl o where o.pid=?1 and o.ptype=?2")
    public List<UmsAcl> getAcl(String pid, String ptype);

    @Query("select o from UmsAcl o where o.rid in (?1) and o.rtype=?2")
    public List<UmsAcl> getAclByResource(String rid, String rtype);

    @Modifying
    @Query("delete from UmsAcl o where o.pid=?1 and o.ptype=?2 and o.rtype=?3")
    public void clearPrincipalResourceAcl(String pid, String ptype, String rtype);

    @Modifying
    @Query("delete from UmsAcl o where o.pid=?1 and o.ptype=?2")
    public void clearPrincipalAcl(String pid, String ptype);

    @Modifying
    @Query("delete from UmsAcl o where o.rid=?1 and o.rtype=?2")
    public void clearResourceAcl(String rid, String rtype);

    @Modifying
    @Query("delete from UmsAcl o where o.pid=?1 and o.ptype=?2 and o.rtype=?3 and o.rid not in ?4")
    public void removeUnAssignedResource(String pid, String ptype, String rtype, String[] rids);


}
