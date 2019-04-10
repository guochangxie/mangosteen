package com.mangosteen.tools;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.SVNFileUtil;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SvnManager {
    private SVNClientManager ourClientManager;
    private SVNURL repositoryOptUrl;
    private String username="XXXXX";
    private String password="XXXXX";
    private Lock lock = new ReentrantLock();

    public void init(){

        try {
            //初始化支持svn://协议的库，必须先执行此操作
            SVNRepositoryFactoryImpl.setup();
            // 初始化支持https://协议的库， 必须先执行此操作
            DAVRepositoryFactory.setup();
            //初始化支持file:///协议的库， 必须先执行此操作
            FSRepositoryFactory.setup();

            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
            ourClientManager = SVNClientManager.newInstance(
                    (DefaultSVNOptions) options, username, password);
        } catch (Exception e) {
            e.printStackTrace();

//            listener.getLogger().println("method=init,{when executing init,we have encountered a error}"+e);
        }
    }

    public void cleanUp(String path) throws Exception{
        lock.lock();
        try{
            SVNWCClient client = ourClientManager.getWCClient();
            File file = new File(path);
            if(file.exists()){
                client.doCleanup(file, true);
                file.delete();
            }
        }finally{
            lock.unlock();
        }
    }

    public SVNCommitInfo move(String source, String desc) throws Exception{
        lock.lock();
        try {
            SVNCopyClient copyClient=ourClientManager.getCopyClient();
            copyClient.setIgnoreExternals(false);
            SVNCopySource[] copySources = new SVNCopySource[1];
            copySources[0] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, SVNURL.parseURIEncoded(source));//SVNRevision.HEAD意味着最新的版本
            return copyClient.doCopy(copySources, SVNURL.parseURIEncoded(desc), true, false, false, null, null);//原资源、目的地址、是否为移动操作、是否创建同样的父目录、目录存在是否表示失败、提交附带信息、
        } finally{
            lock.unlock();
        }
    }

    public SVNCommitInfo copy(String reposityOptUrl,String onlineUrl) throws Exception{
        lock.lock();
        try {
            SVNCopyClient copyClient=ourClientManager.getCopyClient();
            copyClient.setIgnoreExternals(false);
            repositoryOptUrl=SVNURL.parseURIEncoded(reposityOptUrl);
            SVNURL destUrl=SVNURL.parseURIEncoded(onlineUrl);
            SVNCopySource[] copySources = new SVNCopySource[1];
            copySources[0] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, repositoryOptUrl);//SVNRevision.HEAD意味着最新的版本
            return copyClient.doCopy(copySources, destUrl, false, false, false, "copy trunk to deploy", null);//原资源、目的地址、是否为移动操作、是否创建同样的父目录、目录存在是否表示失败、提交附带信息、
        } finally{
            lock.unlock();
        }
    }

    public SVNCommitInfo makeDirectory(String deployUrl) throws Exception{
//    	lock.lock();
        try {
            SVNCommitClient svnClient = ourClientManager.getCommitClient();
            return svnClient.doMkDir(  new SVNURL[] { SVNURL.parseURIEncoded(deployUrl) }, "mkdir");
        } finally{
//			lock.unlock();
        }
    }

    @SuppressWarnings("deprecation")
    public long exportModel(String dirPath,String onlineUrl) throws Exception{
        lock.lock();
        try {
            File outDir=new File(dirPath);
            if(!outDir.exists()){
                outDir.mkdirs();//创建目录
            }
            SVNUpdateClient updateClient=ourClientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);
            SVNURL destUrl=SVNURL.parseURIEncoded(onlineUrl);
            // 执行export 操作，返回工作副本的版本号。
            return updateClient.doExport(destUrl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, "export",false,true);
        } finally{
            lock.unlock();
        }
    }

    public long checkOutModel(String dirPath,String onlineUrl) throws Exception{
        lock.lock();
        try {
            File outDir=new File(dirPath);
            if(!outDir.exists()){
                outDir.mkdirs();//创建目录
            }else {
                System.out.println("本地目录创建失败");
            }
            SVNUpdateClient updateClient=ourClientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);
            SVNURL destUrl=SVNURL.parseURIEncoded(onlineUrl);
            // 执行check out 操作，返回工作副本的版本号。
            return updateClient.doCheckout(destUrl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,false);//INFINITY是递归的导出，false条目存在版本问题时终止checkOut操作
        } finally{
            lock.unlock();
        }
    }

    public long updateModel(File file){

        try {
            SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
            return updateClient.doUpdate(file,SVNRevision.HEAD,SVNDepth.INFINITY, true, false);
        } catch (SVNException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public SVNCommitInfo commit(File commitFile) throws Exception{
        lock.lock();
        try {
            SVNCommitClient client = ourClientManager.getCommitClient();
            return client.doCommit(new File[] { commitFile }, true, "local commit to depository",null,null,true, false, SVNDepth.INFINITY);
        } finally{
            lock.unlock();
        }
    }

    public void merge(String path, File desc) throws Exception{
        lock.lock();
        try {
            SVNDiffClient client = ourClientManager.getDiffClient();
            client.doMergeReIntegrate(SVNURL.parseURIEncoded(path), SVNRevision.HEAD, desc, false);
        } finally{
            lock.unlock();
        }
    }

    public boolean isURLExist(String url){
        lock.lock();
        try {
            SVNURL svnUrl=SVNURL.parseURIEncoded(url);
            SVNRepository svnRepository = SVNRepositoryFactory.create(svnUrl);
            ISVNAuthenticationManager authManager = SVNWCUtil
                    .createDefaultAuthenticationManager(this.username,
                            this.password);
            svnRepository.setAuthenticationManager(authManager);
            SVNNodeKind nodeKind = svnRepository.checkPath("", -1);
            return nodeKind == SVNNodeKind.NONE ? false : true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        } finally{
            lock.unlock();
        }
    }

    public void delete(String url){
        lock.lock();
        try {
            SVNCommitClient commitClient = ourClientManager.getCommitClient();
            commitClient.setIgnoreExternals(false);
            SVNURL deleteUrls[] = new SVNURL[1];
            deleteUrls[0] = SVNURL.parseURIEncoded(url);
            commitClient.doDelete(deleteUrls, "delete model");
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }


    public void doDiff(String url1, String url2, String filePath) throws Exception{
        lock.lock();
        OutputStream outputStream=null;
        try {
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
            //实例化客户端管理类
            ourClientManager = SVNClientManager.newInstance(
                    (DefaultSVNOptions) options, username, password);
            //获得SVNDiffClient类的实例。
            SVNDiffClient diff=ourClientManager.getDiffClient();
             outputStream = SVNFileUtil.openFileForWriting(new File(filePath));
            //SVNRevision.WORKING版本指工作副本中当前内容的版本，SVNRevision.HEAD版本指的是版本库中最新的版本。
            diff.doDiff(SVNURL.parseURIEncoded(url1), SVNRevision.HEAD, SVNURL.parseURIEncoded(url2), SVNRevision.HEAD, SVNDepth.INFINITY, true, outputStream);
            SVNFileUtil.closeFile(outputStream);

        } finally{
            lock.unlock();
            SVNFileUtil.closeFile(outputStream);

        }
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
