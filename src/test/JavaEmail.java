import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 可能遇到的错误:
 * javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
 * 解决方案参见:
 * http://www.oschina.net/question/2282830_247657
 */
public class JavaEmail {
    /**
     * todo support custom email name
     */
    public static void send(String fromMail, String user, String password,
                            List<String> toMails,
                            String mailTitle,
                            String mailContent,
                            String emailServerHost,
                            String emailServerPort
                            ) throws Exception {
        Properties props = new Properties(); //可以加载一个配置文件
        // 使用smtp：简单邮件传输协议
        props.put("mail.smtp.host", emailServerHost); //ConfigurationHelper.emailServerHost());//存储发送邮件服务器的信息
        props.put("mail.smtp.port", emailServerPort); //ConfigurationHelper.emailServerPort());

        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props);//根据属性新建一个邮件会话

        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象
        message.setFrom(new InternetAddress(fromMail));//设置发件人的地址
        int toMailsSize = toMails.size();
        InternetAddress mailAddrs[] = new InternetAddress[toMailsSize];
        for (int i = 0; i < toMails.size(); i++) {
            mailAddrs[i] = new InternetAddress(toMails.get(i));
        }


        message.setRecipients(Message.RecipientType.TO, mailAddrs);//设置收件人,并设置其接收类型为TO
        message.setSubject(MimeUtility.encodeText(mailTitle,MimeUtility.mimeCharset("UTF-8"), null));//设置标题
//        message.setSubject(mailTitle);//设置标题
        //设置信件内容
        message.setContent(mailContent, "text/html;charset=UTF-8"); //发送HTML邮件，内容样式比较丰富
        message.setSentDate(new Date());//设置发信时间
        message.saveChanges();//存储邮件信息

        //发送邮件
        Transport transport = session.getTransport("smtp");
        transport.connect(user, password);
        transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址
        transport.close();
    }
    public static void main(String[] args){
        /*String fromMail = "550345828@qq.com";
        String user = "550345828@qq.com";
        String pwd = "smivpihafemxbddg";
        List<String> toMails = new ArrayList<String>();
//        toMails.add("saifeng.huang@chinapex.com");
        toMails.add("baibin.shang@chinapex.com");
        String mailTitle = "test-ttt";
        String mailContent = "hello hello---";
        String emailServerHost = "smtp.qq.com";
        String emailServerPort = "25";*/


        String fromMail = "service@chinapex.com.cn";
        String user = "service@chinapex.com.cn";
        String pwd = "Chinapex007*123";
        List<String> toMails = new ArrayList<String>();
        toMails.add("saifeng.huang@chinapex.com");
        toMails.add("baibin.shang@chinapex.com");
        String mailTitle = "test-ttt";
        String mailContent = "hello hello---";
        String emailServerHost = "smtp.exmail.qq.com";
        String emailServerPort = "587";
        try {
            send(fromMail, user, pwd, toMails, mailTitle, mailContent, emailServerHost, emailServerPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}