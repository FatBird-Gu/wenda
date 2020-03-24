package com.nowcoder.wenda.contoller;

import com.nowcoder.wenda.entity.Event;
import com.nowcoder.wenda.event.EventProducer;
import com.nowcoder.wenda.util.WendaConstant;
import com.nowcoder.wenda.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShareController implements WendaConstant {
    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private EventProducer eventProducer;
    @Value("${wenda.path.domin}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextParh;
    @Value("${wk.image.storage}")
    private String wkImageStorage;


    //http://127.0.0.1:8080/wenda/share?htmlurl=https://www.nowcoder.com
    @RequestMapping(path = "/share", method = RequestMethod.GET)
    @ResponseBody
    public String share(@RequestParam("htmlurl") String htmlUrl){
        // 文件名
        String fileName = WendaUtil.gennerateUUID();

        // 异步生成长图
        Event event = new Event()
                .setTopic(TOPIC_SHARE)
                .setData("htmlUrl",htmlUrl)
                .setData("filename",fileName)
                .setData("suffix",".png");
        eventProducer.fireEvent(event);

        // 返回访问路径
        Map<String,Object> map = new HashMap<>();
        map.put("shareUrl",domain+contextParh+"/share/image/"+fileName);
        return WendaUtil.getJSONString(0,null,map);
    }

    // 获取长图
    @RequestMapping(path = "/share/image/{filename}",method = RequestMethod.GET)
    public void getShareImage(@PathVariable("filename")String filename, HttpServletResponse response){
        if (StringUtils.isBlank(filename)){
            throw new IllegalArgumentException("文件名不能为空");
        }
        response.setContentType("image/png");
        File file = new File(wkImageStorage+"/"+filename+".png");
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("获取长图失败！"+e.getMessage());
        }

    }
}
