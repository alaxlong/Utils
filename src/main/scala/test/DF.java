package test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/8/17
 * \* Description:
 * \
 */
public class DF {
    private long v;
    public DF(long v){
        this.v = v;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }

    public static DF sum(List<DF> seq) {
        return DF.sum(seq);
    }

    public static void main(String[] args){
        /*List<DF> list = new ArrayList<DF>();
        list.add(new DF(1));
        list.add(new DF(10));
        list.add(new DF(100));
        list.add(new DF(1000));
        long sum = 0;
        for (int i = 0; i < list.size(); i ++){
            sum += list.get(i).getV();
        }
        System.out.println(sum);*/
        byte[] bs1 = {0,97,99,0,0};
        System.out.println(bs1[1]);
        System.out.println(byte2List(bs1));
        System.out.println(list2byte(byte2List(bs1)));

        List a = new ArrayList();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
//        System.out.println(a.subList(0, 3).toArray());

        String[] aa = {"11","22","33"};
        String[] bb = {"44","55","66"};
        String[] cc = {"77","88","99"};

// 合并两个数组
        String[] dd = new String[aa.length + bb.length];
        System.arraycopy(aa, 0, dd, 0, aa.length);
        System.arraycopy(bb, 0, dd, aa.length, bb.length);

//        System.out.println(dd[2]);

        BigInteger[] bt = new BigInteger[]{BigInteger.valueOf(1),BigInteger.valueOf(2)};

        Map ad = new HashMap();
        ad.put(1, 2);
        ad.forEach((k,v)->{

        });
        System.out.println(ad.keySet());

        List list = new ArrayList(10);
        System.out.println(list.size() + "***");
        byte[] bt3 = new byte[5];
        System.out.println(bt3.length);
        System.out.println(new Date());

    }

    public static ArrayList<Byte> byte2List(byte []bt){
        ArrayList<Byte> b = new ArrayList<Byte>();

        for(int i = 0; i < bt.length; i++){
            b.add(bt[i]);
        }
        return b;
    }

    public static byte[] list2byte(ArrayList<Byte> list) {
        if (list == null || list.size() < 0){
            return null;
        }
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }




    private static String Alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static BigInteger Base = new BigInteger("58");

    public static String encode(byte []input) {
        BigInteger bi = new BigInteger(1, input);
        StringBuilder s = new StringBuilder();
        if (bi.intValue() > 0){
            while (bi.intValue() >= Base.intValue()){
                BigInteger mod = bi.mod(Base);
                s.insert(0, Alphabet.charAt(mod.intValue()));
                bi = new BigInteger(String.valueOf((bi.intValue() - mod.intValue()) / Base.intValue()));
            }
            s.insert(0, Alphabet.charAt(bi.intValue()));
        }
        for (int i = 0; i < input.length - 1; i++) {
            if (input[i] == 0) {
                s.insert(0, Alphabet.charAt(0));
            } else {
                break;
            }
        }

        return s.toString();
    }

    public static byte[] randomBytes(int n){
        byte[] bytes = new byte[n];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}