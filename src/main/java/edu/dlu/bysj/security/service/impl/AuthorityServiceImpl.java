package edu.dlu.bysj.security.service.impl;

import edu.dlu.bysj.security.mapper.AuthorityMapper;
import edu.dlu.bysj.security.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/13 20:36
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityMapper authorityMapper;

    @Autowired
    public AuthorityServiceImpl(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    @Override
    public Set<String> studentAuthorization(Integer roleId, LocalDateTime currentTime) {
        return authorityMapper.studentPermissions(roleId, currentTime);
    }

    @Override
    public Set<String> teacherAuthorization(List<Integer> roleIds, LocalDateTime currentTime) {
        return authorityMapper.teacherPermissions(roleIds, currentTime);
    }
}
