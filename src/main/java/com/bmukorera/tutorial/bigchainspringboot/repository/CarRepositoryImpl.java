package com.bmukorera.tutorial.bigchainspringboot.repository;

import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Asset;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Transaction;
import com.bmukorera.tutorial.bigchainspringboot.exception.TransactionException;
import com.bmukorera.tutorial.bigchainspringboot.model.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyPair;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CarRepositoryImpl implements CarRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarRepositoryImpl.class);
    private static final String PUBLIC_KEY = "302a300506032b657003210033c43dc2180936a2a9138a05f06c892d2fb1cfda4562cbc35373bf13cd8ed373";
    private static final String PRIVATE_KEY = "302e020100300506032b6570042204206f6b0cd095f1e83fc5f08bffb79c7c8a30e77a3ab65f4bc659026b76394fcea8";


    private KeyPair keyPair;

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public CarRepositoryImpl() {
        buildKeys();// builds the KeyPair required for accessing and saving transaction to the blockchain using the public and private keys defined

        /*This configures a connection to the Bigchain instance, we are using localhost to connect to a local instance
        * but this can be replaced by any instance you may also have*/
        BigchainDbConfigBuilder
                .baseUrl("http://localhost:9984/")
                .setup();
    }

    @Override
    public Car save(Car car) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> assetData = mapper.convertValue(car, Map.class);//convert Car Object to Map which will be saved as the Asset data
        try {
            Transaction createTransaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
                    .sendTransaction();
            if(LOGGER.isDebugEnabled()){
                LOGGER.info("transaction created \n{}",createTransaction);
            }
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()){
                LOGGER.error("{}",e);
            }
            throw new TransactionException(e);
        }
        return car;
    }

    @Override
    public List<Car> search(String searchKey) {
        List<Car> carList = new ArrayList<>();
        try {
            Assets assets = AssetsApi.getAssets(searchKey);
            List<Asset> assetsList = assets.getAssets();
            assetsList.forEach(asset -> {
                try{
                    Car car = stringToObject(tojson(asset.getData()),Car.class);
                    carList.add(car);
                }catch (Exception e){
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.error("not instance of Car {}",e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()){
                LOGGER.error("{}",e);
            }
            throw new TransactionException(e);
        }
        return carList;
    }

    /* builds the KeyPair required for accessing and saving transaction to the blockchain using the public and private keys defined*/
    private void buildKeys(){
        try{
            X509EncodedKeySpec encoded = new X509EncodedKeySpec(Utils.hexToBytes(PUBLIC_KEY));
            EdDSAPublicKey keyIn = new EdDSAPublicKey(encoded);
            PKCS8EncodedKeySpec encodedP = new PKCS8EncodedKeySpec(Utils.hexToBytes(PRIVATE_KEY));
            EdDSAPrivateKey keyPr = new EdDSAPrivateKey(encodedP);
            setKeyPair(new KeyPair(keyIn,keyPr));
        }
        catch (Exception e){
            if(LOGGER.isDebugEnabled()){
                LOGGER.error("{}",e);
            }
        }
    }


    private String tojson(Object obj){
        ObjectMapper mapper=new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            if(LOGGER.isDebugEnabled()){
                LOGGER.error("{}",e);
            }
            return "";
        }
    }

    public static <T> T stringToObject(String s, Class<T> clazz) {
        return new Gson().fromJson(s, clazz);
    }

}
