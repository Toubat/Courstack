# Courstack
**Coursetack** is an Android Mobile App that help student find or share relevant multimedia learning resources for a specific course, and find classmate sharing common ground.

## User Stories
- [ ] User can login with username and password.
- [ ] User can signup by filling out basic personal information. 
- [ ] User can choose a specfic course forum to join in.
- [ ] Once user discover a piece of useful information he or she appreciate, he or she can offer "stack coin" to the contributor.
- [ ] User can watch a list of short video discussing about certain course content created by other students.
    - [ ] User can leave comments or reply to other's comments.
    - [ ] User can swipe between videos easily.
    - [ ] User can click a video to start watching in a dialog fragment.
- [ ] User can ask questions in different forum for specific class, or share relevant notes.
    - [ ] User add a image along with text under each AnswerPost.
    - [ ] User can search for a specific title or course material they would like to query.
- [ ] User can search for classmate and send private message.
    - [ ] User can filter classmate by courses they are currently taking.
    - [ ] User can view a list of classmate in certain course and read their self-description.

## Networking

List of network requests by screen

* Forum Screen

  * (Read/GET) queryAnswerPost()

    ```
    protected void queryAnswerPost() {
            // Specify which class to query
            ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
            query.include(AnswerPost.KEY_ANSWER);
            query.findInBackground(new FindCallback<AnswerPost>() {
                public void done(List<AnswerPost> items, ParseException e) {
                    if (e == null) {
                        answerPost = items.get(0);
                        queryAnswers(answerPost);
                    } else {
                        Log.e(TAG, "Issue with getting answer posts", e);
                        Toast.makeText(MainActivity.this, "Issue with getting answer posts!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    ```

  * Query Answers under the Answer Post

  * (Read/GET) Query Answers(AnswerPost answerPost)

    ```
    protected void queryAnswers(AnswerPost answerPost) {
            // Specify which class to query
            ParseQuery<Answer> query = ParseQuery.getQuery(Answer.class);
            query.include(Answer.KEY_STUDENT);
            query.include(Answer.KEY_ANSWER_TEXT);
            query.include(Answer.KEY_PARENT);
            query.whereEqualTo(Answer.KEY_PARENT, answerPost);
            query.findInBackground(new FindCallback<Answer>() {
                @Override
                public void done(List<Answer> objects, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting answers", e);
                        Toast.makeText(MainActivity.this, "Issue with getting answers!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "All answers");
                        for (Answer answer: objects) {
                            Log.i(TAG, answer.getText());
                        }
                    }
                }
            });
        }
    ```

  * (Read/GET) forumPostQuery()

    





## Optional Stories
- [ ] User can type Markdown text to stylize their answer posts.

## Model Workflow
<img src='Model Workflow.png' title='Model Worklow' width='' alt='Model Worklow' />

## Activity Layout
<img src='Activity Layout.png' title='Activity Layout' width='' alt='Activity Layout' />
<img src='login.gif' title='Login Activity' width='' alt='Login Activity' />

## License

    Copyright [2021] [xxxxx]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
