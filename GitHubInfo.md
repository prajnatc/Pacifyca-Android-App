# Github Info


git clone git@github.com:vivekiran/pacifyca-android-app.git

git remote -v

origin	git@github.com:vivekiran/pacifyca-android-app.git (fetch)
origin	git@github.com:vivekiran/pacifyca-android-app.git (push)

git remote add upstream git@github.com:Pacesoft/pacifyca-android-app.git

[More info]

https://help.github.com/articles/configuring-a-remote-for-a-fork/


git remote -v

origin	git@github.com:vivekiran/pacifyca-android-app.git (fetch)
origin	git@github.com:vivekiran/pacifyca-android-app.git (push)
upstream	git@github.com:Pacesoft/pacifyca-android-app.git (fetch)
upstream	git@github.com:Pacesoft/pacifyca-android-app.git (push)



pacifyca-android-app git:(master) git branch
* master
➜  pacifyca-android-app git:(master) git checkout -b phase-1
Switched to a new branch 'phase-1'
➜  pacifyca-android-app git:(phase-1) git pull upstream phase-1
From github.com:Pacesoft/pacifyca-android-app
* branch            phase-1    -> FETCH_HEAD
Already up-to-date.
➜  pacifyca-android-app git:(phase-1) git branch
 master
* phase-1


git checkout -b myfeature (A feature you'll be working on its a copy of phase-1 branch)

➜  pacifyca-android-app git:(myfeature) git branch
  master
* myfeature
  phase-1


➜  pacifyca-android-app git:(myfeature) git branch
  master
* myfeature
  phase-1

  pacifyca-android-app git:(myfeature) git status
  On branch myfeature
  nothing to commit, working directory clean
  ➜  pacifyca-android-app git:(myfeature) git status
  On branch myfeature
  Untracked files:
    (use "git add <file>..." to include in what will be committed)

  	GitHubInfo.md

  nothing added to commit but untracked files present (use "git add" to track)


  ➜  pacifyca-android-app git:(myfeature) ✗ git add GitHubInfo.md
  ➜  pacifyca-android-app git:(myfeature) ✗ git status
  On branch myfeature
  Changes to be committed:
    (use "git reset HEAD <file>..." to unstage)

  	new file:   GitHubInfo.md

    ➜  pacifyca-android-app git:(myfeature) ✗ git commit -m "Github info" GitHubInfo.md
    [myfeature da059a5] Github info
     1 file changed, 91 insertions(+)
     create mode 100644 GitHubInfo.md
