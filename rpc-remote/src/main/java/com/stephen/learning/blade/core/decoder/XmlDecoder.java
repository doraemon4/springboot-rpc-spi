package com.stephen.learning.blade.core.decoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:19
 * @Version: 1.0
 */
public class XmlDecoder implements Decoder {
    @Override
    public <T> T decode(String arg, Class<T> returnClass) {
        if(arg == null){
            return null;
        }

        try(ByteArrayInputStream is = new ByteArrayInputStream(arg.getBytes())) {
            JAXBContext context = JAXBContext.newInstance(returnClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Object object = unmarshaller.unmarshal(is);
            System.out.println("使用XmlDecoder解码：" + arg);
            return (T) object;
        } catch (Exception e) {
            throw new IllegalStateException("XmlDecoder解码异常", e);
        }
    }
}
