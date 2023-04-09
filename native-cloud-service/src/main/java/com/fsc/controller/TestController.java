package com.fsc.controller;

import com.fsc.mapper.SysUserMapper;
import com.fsc.model.R;
import com.fsc.model.SysUser;
import com.fsc.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//@Deprecated
@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {

    @GetMapping("/file")
    public void testFile() throws Exception {
        File readFile = new File("C:\\Users\\fangshicai\\Desktop\\书籍\\java8实战目录\\5.png");
        File writeFile = new File("\\\\192.168.43.75\\SharedFolder\\5.png");
        FileInputStream fileInputStream = new FileInputStream(readFile);
        FileOutputStream fileOutputStream = new FileOutputStream(writeFile);
        int b;
        byte[] bytes = new byte[1024];
        while ((b = fileInputStream.read()) != -1) {
            fileInputStream.read(bytes);
        }
        fileOutputStream.write(bytes);
        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     * tar ---> tar.gz
     * @param response
     * @return
     */
    @GetMapping("/CompressGz")
    public Object tarToGzTest(HttpServletResponse response) {
        FileInputStream inputStream = null;
        GzipCompressorOutputStream gzipCompressorOutputStream = null;
        try {
            File file = new File("api-native-cloud/native-cloud-service/src/main/resources/tar/test_nginx-v1.0.tar");
            inputStream = new FileInputStream(file);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + "filename*=UTF-8''" + URLEncoder.encode("abc.tar.gz", StandardCharsets.UTF_8.displayName()));
            response.setContentType("application/octet-stream");
            gzipCompressorOutputStream = new GzipCompressorOutputStream(response.getOutputStream());

            int size = 4096;
            byte[] buffer = new byte[size];
            int n = 0;
            while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
                gzipCompressorOutputStream.write(buffer, 0, buffer.length);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                gzipCompressorOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "ok";
    }

    /**
     * tar.gz ---> tar
     * @param file
     * @return
     */
    @PostMapping("/unCompressGz")
    public Object gzToTarTest(MultipartFile file) {
        try {
            InputStream fin = file.getInputStream();
            BufferedInputStream in = new BufferedInputStream(fin);
            OutputStream out = Files.newOutputStream(Paths.get("api-native-cloud/native-cloud-service/src/main/resources/tar/" + file.getOriginalFilename().replace(".gz", "")));
            GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
            int buffersize = 4096;
            final byte[] buffer = new byte[buffersize];
            int n = 0;
            while (-1 != (n = gzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            gzIn.close();
            out.close();
            in.close();
            fin.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }


//    public static void main(String[] args) throws IOException {
//
//        //开始时间 毫秒
//        long s = System.currentTimeMillis();
//
//        //1.创建一个字节输入流对象，构造方法中绑定要读取的数据源
//        FileInputStream fis = new FileInputStream("C:\\Users\\fangshicai\\Desktop\\书籍\\java8实战目录\\");
//        //2.创建一个字节输出流对象，构造方法中绑定要写入的目的地
//        FileOutputStream fos = new FileOutputStream("\\\\192.168.43.75\\SharedFolder\\11");
//
//
//        //方法二      耗时： 2毫秒
//        //使用数组缓冲多个字节 写入多个字节
//        byte[] bytes = new byte[1024];
//        //3.使用字节输入流对象中的方法read读取文件
//        int len = 0;    //每次读取有效字节数
//        while ((len = fis.read(bytes)) != -1){
//            //4.使用字节输出流中的方法write，把读取到的字节写入到目的地文件中
//            fos.write(bytes,0,len);
//        }
//
//        //释放资源
//        fis.close();
//        fos.close();
//        //结束时间
//        long e = System.currentTimeMillis();
//        System.out.println("这个程序耗时" + (e-s) + "毫秒！");
//
//    }

    public static void main(String[] args) {
        //拷贝源
        File srcFile = new File("C:\\Users\\fangshicai\\Desktop\\书籍\\java8实战目录");
        //拷贝目标
        File destFile = new File("\\\\192.168.43.75\\SharedFolder\\11");
        //调用copyDri方法
        copyDri(srcFile, destFile);
    }

    /**
     * 拷贝目录
     *
     * @param srcFile  拷贝源
     * @param destFile 拷贝目标
     */
    private static void copyDri(File srcFile, File destFile) {
        if (srcFile.isFile()) {
            //src.File

            //是文件就拷贝,且边读边写
            FileInputStream in = null;
            FileOutputStream out = null;
            File file;
            try {
                //读srcFile这个文件
                in = new FileInputStream(srcFile);


                String path = destFile.getAbsolutePath() + srcFile.getAbsolutePath().substring(2);

                out = new FileOutputStream(path);

                //一边读一边写
                //一次性复制1m
                byte[] bytes = new byte[1024 * 1024];
                int readCount = 0;
                while ((readCount = in.read(bytes)) != -1) {
                    out.write(bytes, 0, readCount);
                }

                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //srcFile如果是一个文件,结束递归
            return;
        }
        //获取源下面的子目录(拿到当前文件夹srcFile下的子文件)
        File[] files = srcFile.listFiles();

        //代码测试(写一点测试一点)
        //System.out.println(files.length);

        for (File file : files) {
            //获取所有文件的绝对路径(包括目录和文件)
            //System.out.println(file.getAbsolutePath());
            //这个file可能是文件或者目录

            //
            if (file.isDirectory()) {
                //System.out.println(file.getAbsolutePath());

                String srcDri = file.getAbsolutePath();
                //获取原目标路径,去掉前两位(E:\\学习\\a)---->(\\学习\a)
                //System.out.println(srcDri.substring(2));
                String destDri = destFile.getAbsolutePath() + srcDri.substring(2);

                File newFile = new File(destDri);

                //如果newFile不存在,则新建
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }

                //测试输出文件路径
                //System.out.println(destDri);
            }

            copyDri(file, destFile);
        }
    }

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/getUser")
    public R getUser(){
        SysUser sysUser = sysUserMapper.selectById(1);
        String jwt = jwtUtil.createJWT(sysUser.getId().toString());
        Claims claimsFormToken = jwtUtil.parseJWT(jwt);
        String subject = claimsFormToken.getSubject();
        log.info(subject);
        return R.ok(sysUser.getUsername());
    }
}
