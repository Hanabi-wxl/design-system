import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfTest2 {

    @Test
    public void test() {

        String templatePath = "C:\\Users\\21481\\Desktop\\test1.pdf";
        // 生成的新文件路径
        String newPDFPath = "C:\\Users\\21481\\Desktop\\test2.pdf";

        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;

        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            List<String> list = new ArrayList<String>();

            //首页题目
            String title = "题 目：" + "基于Spring Boot的研究生文档管理系统的设计与实现";
            list.add(title);

            //首页学院
            String college = "：信息工程学院";
            list.add(college);

            //首页班级
            String classes = "：软件工程1801";
            list.add(classes);

            //首页姓名
            String studentName = "：张三";
            list.add(studentName);

            //首页指导老师姓名
            String teacherName = "：李四";
            list.add(teacherName);

            //首页指导老师职称
            String teacherTitle = "：教授";
            list.add(teacherTitle);

            //首页完成时间
            String finishTime = "：2020年6月";
            list.add(finishTime);

            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");
            list.add("男");

            int i = 0;
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                String str = list.get(i);
                i++;
                form.setField(name, str);
            }

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();

            for (int j = 1; j <= 14; j++) {
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), j);
                copy.addPage(importPage);
            }

            doc.close();
            System.out.println(11111111);
        } catch (IOException e) {
            System.out.println(1);
        } catch (DocumentException e) {
            System.out.println(2);
        }
    }
}
