package com.fsc.service;

import com.fsc.model.form.IngressForm;
import com.fsc.model.form.TcpServicePortForm;

public interface IngressApiV1 {
    String exposeTcpService(TcpServicePortForm tcpServicePortForm);

    String ingressBalance(IngressForm ingressForm);
}
