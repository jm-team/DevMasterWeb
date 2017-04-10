package com.jumore.devmaster.common.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * 所有的配置信息都统一放在这里
 * 
 * @author yexinzhou
 * @date 2017年3月24日 上午9:15:10
 * @version
 * @see
 */
public class Configuration {

    @Value(value = "${sys.dataPath}")
    private String dataPath;

    public String getDataPath() {
        return dataPath;
    }

    @Value(value = "${comp.preview.host}")
    private String compPreviewHost;
    
    /**
     *聚灵通短信服务地址
     */
    @Value("${jlt.url}")
    private String jltUrl;

    public String getJltUrl() {
        return jltUrl;
    }
    
    @Value(value = "${docker.host}")
    private String dockerHost;

	public String getDockerHost() {
		return dockerHost;
	}
    @Value(value = "${sys.frontComponent.repoPath}")
    private String frontComponentRepoPath;
    
    @Value(value = "${sys.frontComponent.repoDomain}")
    private String frontComponentRepoDomain;

    public String getFrontComponentRepoPath() {
        return frontComponentRepoPath;
    }

    public String getFrontComponentRepoDomain() {
        return frontComponentRepoDomain;
    }

    public String getCompPreviewHost() {
        return compPreviewHost;
    }
}
