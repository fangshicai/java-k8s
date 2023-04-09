package com.fsc.controller;


import com.fsc.model.R;
import com.fsc.model.form.IngressForm;
import com.fsc.model.form.TcpServicePortForm;
import com.fsc.service.IngressApiV1;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingress")
@Slf4j
@ApiOperation("通过Ingress进行负载均衡")
public class IngressApiV1Controller {

    // 首先必须有负载均衡器，ingress-nginx-controller为nodePort

    @Autowired
    private IngressApiV1 ingressApiV1;

    @ApiOperation(value = "通过ingress实现TCP暴露端口")
    @PostMapping("/exposeTcpService")
    public R<String> exposeTcpService(@ApiParam(value = "ingress表单") TcpServicePortForm tcpServicePortForm){
        String flag = ingressApiV1.exposeTcpService(tcpServicePortForm);
        return R.ok(flag);
    }

    @ApiOperation(value = "Ingress负载均衡，以主机ip形式")
    @PostMapping("/ingress")
    public R<String> ingressBalance(IngressForm ingressForm){
        String str = ingressApiV1.ingressBalance(ingressForm);
        return R.ok("ok");
    }

}
