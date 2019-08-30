package timing.ukulele.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import timing.ukulele.common.data.ResponseData;
import timing.ukulele.web.controller.BaseController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("eureka-client")
public class ServiceRegistryController extends BaseController {

    @Resource
    private ServiceRegistry<EurekaRegistration> serviceRegistry;

    @Resource
    private EurekaRegistration registration;

    @Resource
    private EurekaClient eurekaClient;

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> getStatus() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("status", serviceRegistry.getStatus(registration));
        return successResponse(map);
    }

    @RequestMapping(value = "status", method = RequestMethod.POST)
    public ResponseData<Boolean> setStatus(String status) {
        serviceRegistry.setStatus(registration, status);
        return successResponse(Boolean.TRUE);
    }

    @RequestMapping(value = "status/{appName}", method = RequestMethod.POST)
    public ResponseData<Boolean> status(@PathVariable String appName, String instanceId, String status) {
        Application application = eurekaClient.getApplication(appName);
        InstanceInfo instanceInfo = application.getByInstanceId(instanceId);
        instanceInfo.setStatus(InstanceStatus.toEnum(status));
        return successResponse(Boolean.TRUE);
    }
}
