package com.starfire.familytree.folk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.enums.BooleanEnum;
import com.starfire.familytree.folk.entity.Children;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.mapper.ChildrenMapper;
import com.starfire.familytree.folk.mapper.MemberMapper;
import com.starfire.familytree.folk.service.IChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luzh
 * @since 2019-08-15
 */
@Service
public class ChildrenServiceImpl extends ServiceImpl<ChildrenMapper, Children> implements IChildrenService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<Member> getChildrenList(Long parentId) {
        return baseMapper.getChildrenList(parentId);
    }

    @Override
    public List<Member> getJuniorList(Long parentId) {
        List<Member> juniorList=new ArrayList<>();
        List<Member> childrenList = getChildrenList(parentId);
        juniorList.addAll(childrenList);
        for (int i = 0; i < childrenList.size(); i++) {
            Member member =  childrenList.get(i);
            Long memberId = member.getId();
            List<Member> memberList = getChildrenList(memberId);
            juniorList.addAll(memberList);
        }
        return juniorList;
    }

}
