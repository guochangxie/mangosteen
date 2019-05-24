package com.mangosteen.service.impl;

import com.mangosteen.service.CodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/4/85:01 PM
 */
@Service
@Slf4j
public class GitRepositoryImpl implements CodeRepository {

    private  String userName="guochang.xie";

    private  String passwd="xxxxxxxxx";

    @Override
    public boolean downLoad(File repository, String branch) {
        try {
            CloneCommand cloneCommand = Git.cloneRepository().setURI(branch);
            cloneCommand.setDirectory(repository);
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName,passwd));
            cloneCommand.call();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("git clone fail,errorMessage:{}",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(File file, String branch) {

        CheckoutCommand checkout = null;
        try {
            checkout = Git.open(file).checkout();
            Ref ref = checkout.getRepository().findRef(branch);
            if(ref!=null){
                checkout.setName(branch).call();
            }else{
                checkout.setName(branch).
                        setCreateBranch(true).
                        setStartPoint("origin/"+branch).
                        setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                        setStartPoint("origin/" + branch).call();
            }

            PullCommand pullCommand = Git.open(file).pull();
            pullCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName,passwd));
            pullCommand.call();
        } catch (Exception e) {

            log.error("git checkoutAndPull fail,errorMessage:{}",e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<String> diff(String baseUrl, String diffUrl) {
        List<String> changeClass=new ArrayList<>();
        try {
            DiffCommand diff = Git.open(new File(diffUrl)).diff();
            Repository repository = diff.getRepository();
            AbstractTreeIterator newTree = prepareTreeParser(repository, "refs/heads/" + baseUrl);
            AbstractTreeIterator oldTree = prepareTreeParser(repository, "refs/heads/master");
            List<DiffEntry> diffEntries = diff.setNewTree(newTree).setOldTree(oldTree).call();

            for (DiffEntry entry : diffEntries) {
                String changeFile=null;
                if ("ADD".equals(entry.getChangeType().toString())){
                    changeFile=entry.getNewPath();
                }else{
                    changeFile = entry.getOldPath();

                }
                if(changeFile.endsWith(".java")){
                    changeClass.add(StringUtils.substringAfterLast(changeFile,"/").replace(".java",".class"));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return changeClass;
    }


    private  AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        Ref head = repository.exactRef(ref);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }



}
