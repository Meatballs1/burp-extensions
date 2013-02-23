Burp Suite Extensions Collection
===============

A collection of extensions for the new Burp Suite API (v1.5+) using Submodules for easy collection and updating. If you want to add a new module to the collection just send a Pull request or create an Issue. If you want your collection removed create an Issue. 

Thanks to Mubix for inspiration (https://github.com/mubix/tools) :)

Get started

After you pulled the repo you'll notice that none of the "submodule" repos have any files in them.

To get started you need to perform the following:

	git submodule init - initialize the submodules
	git submodule update - pull & update the repos to current version
	
Update

	git submodule update - update the repos to current version

Included:

BurpJDSer-ng
===============
A Burp Extender plugin, that will deserialized java objects and encode them in XML using the Xtream library.

BurpAuthzPlugin
===============
Burp plugin to test for authorization flaws