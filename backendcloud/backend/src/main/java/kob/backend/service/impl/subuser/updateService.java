package kob.backend.service.impl.subuser;

import kob.backend.mapper.SubuserMapper;
import kob.backend.pojo.Subuser;
import kob.backend.pojo.User;
import kob.backend.service.impl.utils.UserDetailImpl;
import kob.backend.service.user.subuser.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class updateService implements UpdateService {

    @Autowired
    private SubuserMapper subuserMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();

        Integer subuser_id = Integer.parseInt(data.get("subuser_id"));
        String subbotName = data.get("subbotName");
        String description = data.get("description");
        String content = data.get("content");


        if(subbotName == null || subbotName.length() == 0){
            map.put("message", "subbotName为空");
            return map;
        }
        if(subbotName.length() > 100){
            map.put("message", "subbotName长度不能超过100");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户很懒,什么也没留下~";
        }
        if(description.length() > 300){
            map.put("message", "description长度不能超过300");
            return map;
        }

        if(content == null || content.length() == 0){
            map.put("message", "content为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("message", "content长度不能超过10000");
            return map;
        }

        Subuser subuser = subuserMapper.selectById(subuser_id);
        if(subuser == null){
            map.put("message", "subuser不存在");
            return map;
        }
        if(!subuser.getUserId().equals(user.getId())){
            map.put("message", "只能操作自己的subuser");
            return map;
        }
        Subuser subuser_updated = new Subuser(
                subuser.getId(),
                user.getId(),
                subbotName,
                description,
                content,
                subuser.getCreateTime(),
                new Date()
        );

        subuserMapper.updateById(subuser_updated);
        map.put("message", "success");
        return map;
    }
}
