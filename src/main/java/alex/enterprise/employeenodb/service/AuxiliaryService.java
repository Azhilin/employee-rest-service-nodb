package alex.enterprise.employeenodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuxiliaryService {

    @Value("${auxiliary.server.url}")
    private String auxiliaryServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String addId(Integer id) {
        String fullUrl = getFullUrl(id);
        restTemplate.put(fullUrl, null);
        return fullUrl;
    }

    public Boolean getByUrl(String url) {
        return restTemplate.getForObject(url, Boolean.class);
    }

    private String getFullUrl(Integer id) {
        return String.format("%s/%s", auxiliaryServerUrl, id);
    }
}
