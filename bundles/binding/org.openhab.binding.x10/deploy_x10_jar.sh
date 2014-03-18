#!/bin/bash
cd /Users/Benji/git/polyprotopi_openhab/bundles/binding/org.openhab.binding.x10
#mvn clean install
cd target
sudo sshpass -p xoRet1334 sftp benji@pppbeagle.student.rit.edu << !
	cd /ha/openHAB/addons
	put org.openhab.binding.x10-1.4.0-SNAPSHOT.jar
	exit
!