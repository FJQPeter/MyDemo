package com.instant.datastorage.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * Created by nijie on 2016/8/3.
 */
public class RuleLoadTool {

    private static final Logger logger = Logger.getLogger(RuleLoadTool.class);

    private static Map<String,Long> ruleFiles = new HashMap();

    public static void loadRules(){
        List<File> ruleFiles = getRulesFiles();
        if(ruleFiles.size() == 0){
            return;
        }
        logger.info("start load rule files...");
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        for (File ruleFile : ruleFiles) {
            kbuilder.add(ResourceFactory.newFileResource(ruleFile),ResourceType.DRL);
            if(kbuilder.hasErrors()){ 
                System.out.println("规则中存在错误，错误消息如下："); 
                KnowledgeBuilderErrors kbuidlerErrors=kbuilder.getErrors();
                for(Iterator iter=kbuidlerErrors.iterator();iter.hasNext();){ 
                   System.out.println(iter.next()); 
                } 
            } 
            logger.info("rule file : "+ruleFile.getAbsolutePath());
        }

        Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        kbase.addKnowledgePackages(pkgs);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        DroolsFactory.getInstance().setKieSession(ksession);
        logger.info("load drools rules finish.");
    }

    /**
     * 获取需要重新加载的配置文件路径
     * @return
     */
    public static List<File> getRulesFiles(){
        String ruleFileFolder = RuleLoadTool.class.getResource("/rules").getFile();
        File folder = new File(ruleFileFolder);
        if(!folder.exists())
            throw new RuntimeException("rule files folder is not exist.");
        List<File> allDrlFiles = new ArrayList();
        getDrlFiles(allDrlFiles,folder);
        boolean hasFileChanged = false;//是否有文件发生变更，包括新增、修改、删除
        //文件数量发生变化
        if(allDrlFiles.size() != ruleFiles.size()){
            hasFileChanged = true;
        }else{
            for(File file : allDrlFiles){
                Long time = ruleFiles.get(file.getAbsolutePath());
                if(time == null || time.longValue() != file.lastModified()){
                    hasFileChanged = true;
                    break;
                }
            }
        }
        if(hasFileChanged){
            Map<String,Long> tmp = new HashMap();
            for(File file : allDrlFiles){
                tmp.put(file.getAbsolutePath(),file.lastModified());
            }
            ruleFiles = tmp;
            return allDrlFiles;
        }
        return Collections.EMPTY_LIST;
    }

    private static void getDrlFiles(List<File> fileList, File folder){
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                getDrlFiles(fileList,file);
            }else{
                fileList.add(file);
            }
        }
    }
}
