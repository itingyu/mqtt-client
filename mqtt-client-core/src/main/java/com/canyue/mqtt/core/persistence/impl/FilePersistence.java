package com.canyue.mqtt.core.persistence.impl;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.persistence.IPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author canyue
 */
public class FilePersistence implements IPersistence {
    private String userDir = System.getProperty("user.dir");
    private File dataDir = null;
    private File workDir = null;
    private final Logger logger= LoggerFactory.getLogger(FilePersistence.class);
    public FilePersistence(String dirString){
        dataDir = new File(dirString);
        logger.info("使用数据目录:{}",dataDir.getAbsolutePath());
    }

    @Override
    public void open(String clientId) throws MqttPersistenceException {
        if(dataDir.exists()&&!dataDir.isDirectory()){
            throw new MqttPersistenceException("不是目录但已存在!");
        }else if(!dataDir.exists()){
            dataDir.mkdirs();
        }
        if(workDir==null){
            workDir=new File(dataDir,clientId.replaceAll("-",""));
            workDir.mkdirs();
        }
        logger.info("workDir已打开:{}",workDir.getAbsolutePath());
    }

    @Override
    public void close() throws MqttPersistenceException {
        dataDir=null;
        workDir=null;
        logger.info("persistence已关闭");
    }

    @Override
    public void clear() throws MqttPersistenceException {

        if(workDir!=null){
            File[] files = workDir.listFiles();
            for (File file : files) {
                file.delete();
            }
            workDir.delete();
            logger.info("会话已清理");
        }else {
            logger.info("没有需要清理的会话");
        }

    }

    @Override
    public Object find(String filename) throws MqttPersistenceException {
        File file = new File(workDir,filename);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<BasePacket> getAllNeed2Retry() throws MqttPersistenceException {
        ArrayList<BasePacket> al = new ArrayList<>();
        File[] files = workDir.listFiles();
        for (File file : files) {
            if(file.getName().endsWith(".p")){
                al.add(new PublishPacket((Message) find(file.getName())));
            }else if(file.getName().endsWith(".pack")){
                al.add(new PubAckPacket((Integer) find(file.getName())));
            }else if(file.getName().endsWith(".prec")){
                al.add(new PubRecPacket((Integer) find(file.getName())));
            }else if(file.getName().endsWith(".prel")){
                al.add(new PubRelPacket((Integer) find(file.getName())));
            }else if(file.getName().endsWith(".pcomp")){
                al.add(new PubCompPacket((Integer) find(file.getName())));
            }
        }
        return  al;
    }


    @Override
    public void save(String filename, Object o) throws MqttPersistenceException {
        File file = new File(workDir,filename);
        logger.debug("{}已保存",filename);
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
        } catch (IOException e) {
           throw new MqttPersistenceException("save failed！",e);
        }
    }

    @Override
    public void remove(String filename) throws MqttPersistenceException {
        File file=new File(workDir,filename);
        if(file!=null){
            file.delete();
            logger.debug("{}已移除",filename);
        }
    }
}
