package com.rjsoft.uums.facade.jwt.service.impl;

import com.rjsoft.uums.core.jwt.service.UmsJwtService;
import com.rjsoft.uums.facade.jwt.domain.Payload;
import com.rjsoft.uums.facade.jwt.service.UmsJwtFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umsJwtFacadeImpl")
public class UmsJwtFacadeImpl implements UmsJwtFacade {

    @Autowired
    private UmsJwtService umsJwtService;

    @Override
    public String createJwt(String username, boolean remember) throws Exception {
        return umsJwtService.createJwt(username, remember);
    }

    @Override
    public void removeJwt(String username, String jwt) throws Exception {
        umsJwtService.removeJwt(username, jwt);
    }

    @Override
    public Payload getPayload(String jwt) {
        return umsJwtService.getPayload(jwt);
    }
}
