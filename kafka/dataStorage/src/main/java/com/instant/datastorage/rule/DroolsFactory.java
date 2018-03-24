package com.instant.datastorage.rule;

import org.kie.api.runtime.KieSession;


/**
 * Created by nijie on 2016/8/1.
 */
public class DroolsFactory {


    private static DroolsFactory droolsFactory = new DroolsFactory();

    private KieSession kieSession;

    private DroolsFactory(){
    }

    public static DroolsFactory getInstance(){
        return droolsFactory;
    }

    public KieSession getKieSession() {
        return kieSession;
    }

    public void setKieSession(KieSession kieSession) {
        this.kieSession = kieSession;
    }


}
