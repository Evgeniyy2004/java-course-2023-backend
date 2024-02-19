package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.configuration.ClientConfiguration;
import lombok.extern.java.Log;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

@WireMockTest
@Log
public class ClientTest {
    WireMockServer wireMockServer = new WireMockServer();

    @Test
    public  void gitTest() {
        //Arrange
        wireMockServer.start();
        stubFor(get(urlEqualTo("https://api.github.com/repos/jojozhuang/algorithm-problems-java"))
            .willReturn(aResponse().withBody(
                "{" +
                    "  \"id\": 43661375,\n" +
                    "  \"node_id\": \"MDEwOlJlcG9zaXRvcnk0MzY2MTM3NQ==\",\n" +
                    "  \"name\": \"algorithm-problems-java\",\n" +
                    "  \"full_name\": \"jojozhuang/algorithm-problems-java\",\n" +
                    "  \"private\": false,\n" +
                    "  \"owner\": {\n" +
                    "    \"login\": \"jojozhuang\",\n" +
                    "    \"id\": 11085641,\n" +
                    "    \"node_id\": \"MDQ6VXNlcjExMDg1NjQx\",\n" +
                    "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/11085641?v=4\",\n" +
                    "    \"gravatar_id\": \"\",\n" +
                    "    \"url\": \"https://api.github.com/users/jojozhuang\",\n" +
                    "    \"html_url\": \"https://github.com/jojozhuang\",\n" +
                    "    \"followers_url\": \"https://api.github.com/users/jojozhuang/followers\",\n" +
                    "    \"following_url\": \"https://api.github.com/users/jojozhuang/following{/other_user}\",\n" +
                    "    \"gists_url\": \"https://api.github.com/users/jojozhuang/gists{/gist_id}\",\n" +
                    "    \"starred_url\": \"https://api.github.com/users/jojozhuang/starred{/owner}{/repo}\",\n" +
                    "    \"subscriptions_url\": \"https://api.github.com/users/jojozhuang/subscriptions\",\n" +
                    "    \"organizations_url\": \"https://api.github.com/users/jojozhuang/orgs\",\n" +
                    "    \"repos_url\": \"https://api.github.com/users/jojozhuang/repos\",\n" +
                    "    \"events_url\": \"https://api.github.com/users/jojozhuang/events{/privacy}\",\n" +
                    "    \"received_events_url\": \"https://api.github.com/users/jojozhuang/received_events\",\n" +
                    "    \"type\": \"User\",\n" +
                    "    \"site_admin\": false\n" +
                    "  },\n" +
                    "  \"html_url\": \"https://github.com/jojozhuang/algorithm-problems-java\",\n" +
                    "  \"description\": \"Solution for algorithm problems on leetcode, lintcode and hackerrank.\",\n" +
                    "  \"fork\": false,\n" +
                    "  \"url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java\",\n" +
                    "  \"forks_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/forks\",\n" +
                    "  \"keys_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/keys{/key_id}\",\n" +
                    "  \"collaborators_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/collaborators{/collaborator}\",\n" +
                    "  \"teams_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/teams\",\n" +
                    "  \"hooks_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/hooks\",\n" +
                    "  \"issue_events_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/issues/events{/number}\",\n" +
                    "  \"events_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/events\",\n" +
                    "  \"assignees_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/assignees{/user}\",\n" +
                    "  \"branches_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/branches{/branch}\",\n" +
                    "  \"tags_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/tags\",\n" +
                    "  \"blobs_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/git/blobs{/sha}\",\n" +
                    "  \"git_tags_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/git/tags{/sha}\",\n" +
                    "  \"git_refs_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/git/refs{/sha}\",\n" +
                    "  \"trees_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/git/trees{/sha}\",\n" +
                    "  \"statuses_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/statuses/{sha}\",\n" +
                    "  \"languages_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/languages\",\n" +
                    "  \"stargazers_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/stargazers\",\n" +
                    "  \"contributors_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/contributors\",\n" +
                    "  \"subscribers_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/subscribers\",\n" +
                    "  \"subscription_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/subscription\",\n" +
                    "  \"commits_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/commits{/sha}\",\n" +
                    "  \"git_commits_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/git/commits{/sha}\",\n" +
                    "  \"comments_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/comments{/number}\",\n" +
                    "  \"issue_comment_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/issues/comments{/number}\",\n" +
                    "  \"contents_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/contents/{+path}\",\n" +
                    "  \"compare_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/compare/{base}...{head}\",\n" +
                    "  \"merges_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/merges\",\n" +
                    "  \"archive_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/{archive_format}{/ref}\",\n" +
                    "  \"downloads_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/downloads\",\n" +
                    "  \"issues_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/issues{/number}\",\n" +
                    "  \"pulls_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/pulls{/number}\",\n" +
                    "  \"milestones_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/milestones{/number}\",\n" +
                    "  \"notifications_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/notifications{?since,all,participating}\",\n" +
                    "  \"labels_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/labels{/name}\",\n" +
                    "  \"releases_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/releases{/id}\",\n" +
                    "  \"deployments_url\": \"https://api.github.com/repos/jojozhuang/algorithm-problems-java/deployments\",\n" +
                    "  \"created_at\": \"2015-10-05T02:20:16Z\",\n" +
                    "  \"updated_at\": \"2021-02-05T05:42:24Z\",\n" +
                    "  \"pushed_at\": \"2021-02-05T05:42:19Z\",\n" +
                    "  \"git_url\": \"git://github.com/jojozhuang/algorithm-problems-java.git\",\n" +
                    "  \"ssh_url\": \"git@github.com:jojozhuang/algorithm-problems-java.git\",\n" +
                    "  \"clone_url\": \"https://github.com/jojozhuang/algorithm-problems-java.git\",\n" +
                    "  \"svn_url\": \"https://github.com/jojozhuang/algorithm-problems-java\",\n" +
                    "  \"homepage\": \"\",\n" +
                    "  \"size\": 5551,\n" +
                    "  \"stargazers_count\": 3,\n" +
                    "  \"watchers_count\": 3,\n" +
                    "  \"language\": \"Java\",\n" +
                    "  \"has_issues\": true,\n" +
                    "  \"has_projects\": true,\n" +
                    "  \"has_downloads\": true,\n" +
                    "  \"has_wiki\": true,\n" +
                    "  \"has_pages\": false,\n" +
                    "  \"has_discussions\": false,\n" +
                    "  \"forks_count\": 2,\n" +
                    "  \"mirror_url\": null,\n" +
                    "  \"archived\": false,\n" +
                    "  \"disabled\": false,\n" +
                    "  \"open_issues_count\": 1,\n" +
                    "  \"license\": null,\n" +
                    "  \"allow_forking\": true,\n" +
                    "  \"is_template\": false,\n" +
                    "  \"web_commit_signoff_required\": false,\n" +
                    "  \"topics\": [\n" +
                    "\n" +
                    "  ],\n" +
                    "  \"visibility\": \"public\",\n" +
                    "  \"forks\": 2,\n" +
                    "  \"open_issues\": 1,\n" +
                    "  \"watchers\": 3,\n" +
                    "  \"default_branch\": \"master\",\n" +
                    "  \"temp_clone_token\": null,\n" +
                    "  \"network_count\": 2,\n" +
                    "  \"subscribers_count\": 2\n" +
                    "}"
            )));
        //Act
        var response = new ClientConfiguration().beanGit().fetchRepository("jojozhuang","algorithm-problems-java");
        //Assert
        assertThat(response.owner.user()).isEqualTo("jojozhuang");
        assertThat(response.name).isEqualTo("algorithm-problems-java");
        wireMockServer.stop();
    }

    @Test
    public  void stackTest() {
        var response = new ClientConfiguration().beanStack().fetchQuestion(6827752);
        log.info(String.valueOf(response.isDone));

    }
}

