package timing.ukulele.web.service;

import java.util.List;
import java.util.Set;

/**
 * •权限角色接口
 * •@className: PermissionService
 * •@author: 吕自聪
 * •@date: 2019/9/29
 */
public interface PermissionService {
    Set<String> abilitySet(String router, List<String> acl);
}