package com.jumore.devmaster.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.jumore.devmaster.common.util.SessionHelper;
import com.jumore.devmaster.entity.Project;
import com.jumore.dove.controller.base.BaseController;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/container")
public class ContainerController extends BaseController {

    @Autowired
    private BaseService  baseService;

    private DockerClient dockerClient;

    {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(SessionHelper.getDockerHost())
                .build();

        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory().withReadTimeout(1000).withConnectTimeout(1000)
                .withMaxTotalConnections(100).withMaxPerRouteConnections(10);

        dockerClient = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();
    }

    @ResponseBody
    @RequestMapping(value = "/generateContainer")
    public ResponseVo<String> generateContainer(Long projectId) throws Exception {
        Project projectPo = baseService.get(Project.class, projectId);
        if (projectPo == null) {
            throw new RuntimeException("工程不存在");
        }

        List<String> nameFilter = new ArrayList<>();
        nameFilter.add(projectPo.getName());
        ListContainersCmd cmd = dockerClient.listContainersCmd();
        cmd.withShowAll(true);
        cmd.getFilters().put("name", nameFilter);

        List<Container> queryResult;
        try {
            queryResult = cmd.exec();
        } catch (Exception e) {
            logHelper.getBuilder().error("Docker命令执行失败：", e);
            throw new RuntimeException("生成容器失败。");
        }

        if (queryResult != null && queryResult.size() > 0) {
            throw new RuntimeException("容器已创建。不能重复创建。");
        }

        CreateContainerResponse newContainer = dockerClient.createContainerCmd("tomcat").withName(projectPo.getName()).exec();

        return ResponseVo.<String>BUILDER().setDesc("容器已创建。ID：" + newContainer.getId()).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
