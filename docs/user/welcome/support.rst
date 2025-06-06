Communication and Support
=========================

Email
-----

The GeoTools Users email list is a great place to start out; ask questions and share tips and tricks on using the library.

The users list is for questions regarding the installation or use of the GeoTools library. Users of GeoTools are usually Java developers building applications with spatial capabilities.

* https://lists.sourceforge.net/lists/listinfo/geotools-gt2-users
* https://sourceforge.net/mailarchive/forum.php?forum_name=geotools-gt2-users

Although GeoTools is primarily a Java library, it is also used to host some small tools or server-side code (for example a refactoring script, or a format conversion utility). If these have not yet found a project of their own, then questions about how to use these tools may be directed to this list.

Developer Chat
--------------

GeoTools has a developer chat on Gitter:

* `https://matrix.to/#/#geotools_geotools:gitter.im <https://matrix.to/#/#geotools_geotools:gitter.im>`__

This is used on occasion when email is too slow - this is also a suitable venue for development questions.

Question and Answer Forums
--------------------------

While question and answer forums are not an official means of communication they do offer an advantage over the traditional email list in that good answers can be voted relevant.

Tips for use:

* Search before asking a question
* If you find nobody is answering send a email to the user list.
* Remember to browse the email archive

Here are some forums we are aware of at this time:

* https://gis.stackexchange.com/questions/tagged/geotools
* https://stackoverflow.com/questions/tagged/geotools

.. _commercial-support:

Commercial Support
------------------

If you seriously have a deadline, or find you are spending too much time reading this documentation
please consider setting up a support contract with any of the following organizations.

* `Camptocamp <http://www.camptocamp.com/en/services/support>`_
* `GeoSolutions <https://www.geo-solutions.it/offer/>`_
* `Ian Turton, UK <https://www.ianturton.com>`_

Please consider this as an option, especially if you do not have a background in Geospatial Science
or stuck between a bug and a deadline. Remember that the documentation is constructed by volunteers,
or people like you hiring an organization to for specific work.

Issue Tracker
-------------

GeoTools tracks tasks, issues and bugs with its JIRA tracker, generously provided by OSGeo and hosted by Atlassian. This is where all bugs should be reported, in addition to requested features
and improvements.

To create an issue:

1. `Log In <https://osgeo-org.atlassian.net/login>`__ to ``<osgeo-org.atlassian.net>``.
   
   * If this is your first time you can `create an account <https://osgeo-org.atlassian.net/admin/users/sign-up>`__ using the link at the bottom of the Sign In screen.

3. Once logged in you can `Create <https://osgeo-org.atlassian.net/secure/CreateIssue!default.jspa>`__ an issue from the button at the top of the page
   
   * Navigate to the `GeoTools JIRA page <https://osgeo-org.atlassian.net/projects/GEOT>`_
   * Press the **Create** at the top of the page
   
   .. image:: /images/CreateIssue.png
   
4. When creating a new issue be sure to fill out all the
   relevant fields.
   
   * Filling out all fields is specially important for bug reports, including components, affected versions, and environment
   * You may wish to attach log files, or screen snapshots to the Jira task

   .. image:: /images/CreateIssueDetails.png
      :width: 440
      :height: 447
      
5. Tips for a useful bug report

   * Besides a description of the problem, some additional information can make the
     difference between your issue being looked at or it being put off for a rainy
     day
   * The version of GeoTools you are using? (a release or nightly build?)
   * Example code that reproduces the problem?
   * A stack trace indicating where the failure occurred?
   * The Java Version and operating system you are using?

6. What Happens Next?
   
   On creation a notification will automatically be sent to the GeoTools
   Developers list.
   JIRA sends notifications for everything done on the issue, to the reporter, the
   assignee, and to anyone who clicks on the link to 'watch' an issue.
   
   Note: This is why you must sign up for an account, so that JIRA can email you
   when updates are done. Your email will not be used for anything else. One nice
   little feature of JIRA is that if you reply to the email sent for notification,
   then the reply will show up as a comment on the issue.

7. When will your bug be fixed?
   
   Well for the above bug report Andrea Aime (the module maintainer) will get
   assigned the bug by default, and will probably respond with a nice email
   explaining the problem.::
     
     Not all platforms support Java 2D antialiasisng; wait a moment
     the Amiga 2000 does not support java?
     
     -------------------------------------------------------
     Ing. Andrea Aime
     GeoSolutions S.A.S.
     Tech lead
   
   And then the bug will be "CANNOT REPRODUCE", and Jira will tell you about that too.

8. For New Bugs we may need assistance to reproduce the issue before a fix can be considered.
   
   Usually the Developer (or the Module Maintainer) will need to ask you for more information.
   Until they can reproduce your issue, or you volunteer to test, not much is going to happen.
   
   Example: If you are on some exotic hardware (like oracle on windows server) that we do not
   have public access to you will probably need to arrange to meet on IRC and test out different
   solutions with a developer.
   
9. For new bugs with a Patch.
   
   You can attach a code patch to the Jira task and ask the module maintainer to include
   your fix in the next release.
   
   * For fixes: please include a JUnit test case showing that your fix does something. If a developer needs
     to spend 30 minutes reproducing your problem in order to show your fix addresses the issue it will
     be put off for another day.
   
   * For improvements: You may be asked to attend a IRC chat to thrash out ideas on how best to include
     you great idea.
   
10. Why Volunteering to Test Makes a Difference
    
    Remember that even volunteering to test makes a HUGE difference for developers ... it literally
    cuts down the work by two thirds!
   
    If you are available to test:
    
    * The module maintainer does not have to spend time trying to reproduce the problem (you already have it!)
    * The module maintainer can focus on the code in front of them, you can verify the fixed worked
    
    Even if you cannot test right away, swapping messages on email or trying out nightly builds can make a difference.

11. Keep in mind that many members of the GeoTools library have a day job. Just as you volunteer your time
    to report an issue, they volunteer their time to support the library.
