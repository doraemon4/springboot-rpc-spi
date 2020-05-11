package com.stephen.learning.blade.core.encoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 14:43
 * @Version: 1.0
 */
public class XmlEncoder implements Encoder{
    @Override
    public Object encode(Object arg) {
        if(arg == null){
            return null;
        }

        try(StringWriter writer = new StringWriter()) {
            JAXBContext context = JAXBContext.newInstance(arg.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(arg, writer);
            System.out.println("使用XmlEncoder编码：" + arg);
            return writer.toString();
        } catch (Exception e) {
            throw new IllegalStateException("xml编码异常", e);
        }
    }
}
