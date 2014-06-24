#!/bin/bash

# RM STUFF
ensemblePropertiesDirectory=`ls -t /msop/psi/plugins/ | grep "gov\.nasa\.ensemble\.phoenix\.rcp_.*" | tail -1`
ensemblePropertiesFilePath="/msop/psi/plugins/${ensemblePropertiesDirectory}/ensemble.properties"	

rm $ensemblePropertiesFilePath


rm /msop/psi/PSI.ini  
rm /msop/psi/configuration/config.ini 

rm /msop/psi_messagereactor/conf/config.pl 
rm /msop/psi_messagereactor/conf/PSI_MessageReactor.xml 
rm /msop/psi_messagereactor/tools/do-pipeline.config 
rm /msop/psi_messagereactor/tools/catalogPopulator/catalogPopulator.ini 
rm /msop/psi_messagereactor/tools/catalogPopulator/configuration/config.ini 
rm /msop/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/ensemble.properties 

catPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/catalogPopulator/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
catPropertiesFilePath="/msop/psi_messagereactor/tools/catalogPopulator/plugins/${catPropertiesDirectory}/defaultLog4j.properties"
rm "$catPropertiesFilePath"


rm /msop/psi_messagereactor/tools/harmonic/harmonic.ini 
rm /msop/psi_messagereactor/tools/harmonic/configuration/config.ini 
rm /msop/psi_messagereactor/tools/harmonic/dir-wrap-harmonic 
rm /msop/psi_messagereactor/tools/phxMosaicBuilder/mosaicker.ini 
rm /msop/psi_messagereactor/tools/phxMosaicBuilder/configuration/config.ini 
rm /msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties 

builderPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
builderPropertiesFilePath="/msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/${builderPropertiesDirectory}/defaultLog4j.properties"
rm "$builderPropertiesFilePath"

rm /msop/psi_messagereactor/tools/phxMosaicServer/mosaicServer.ini 
rm /msop/psi_messagereactor/tools/phxMosaicServer/configuration/config.ini 
rm /msop/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties 

serverPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/phxMosaicServer/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
serverPropertiesFilePath="/msop/psi_messagereactor/tools/phxMosaicServer/plugins/${serverPropertiesDirectory}/defaultLog4j.properties"	
rm "$serverPropertiesFilePath"
rm /msop/psi_messagereactor/tools/scripts/mosaic_request.config 

# LN -s
ln -s /msop/pes_rw/psi/ensemble.properties "$ensemblePropertiesFilePath"

ln -s /msop/pes_rw/psi/PSI.ini /msop/psi/PSI.ini  
ln -s /msop/pes_rw/psi/configuration/config.ini /msop/psi/configuration/config.ini 

ln -s /msop/pes_rw/psi_messagereactor/conf/config.pl /msop/psi_messagereactor/conf/config.pl
ln -s /msop/pes_rw/psi_messagereactor/conf/PSI_MessageReactor.xml /msop/psi_messagereactor/conf/PSI_MessageReactor.xml
ln -s /msop/pes_rw/psi_messagereactor/tools/do-pipeline.config /msop/psi_messagereactor/tools/do-pipeline.config
ln -s /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/catalogPopulator.ini /msop/psi_messagereactor/tools/catalogPopulator/catalogPopulator.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/configuration/config.ini /msop/psi_messagereactor/tools/catalogPopulator/configuration/config.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/ensemble.properties /msop/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/ensemble.properties

ln -s /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.ensemble.common/defaultLog4j.properties "$catPropertiesFilePath"


ln -s /msop/pes_rw/psi_messagereactor/tools/harmonic/harmonic.ini /msop/psi_messagereactor/tools/harmonic/harmonic.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/harmonic/configuration/config.ini /msop/psi_messagereactor/tools/harmonic/configuration/config.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/harmonic/dir-wrap-harmonic /msop/psi_messagereactor/tools/harmonic/dir-wrap-harmonic
ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/mosaicker.ini /msop/psi_messagereactor/tools/phxMosaicBuilder/mosaicker.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/configuration/config.ini /msop/psi_messagereactor/tools/phxMosaicBuilder/configuration/config.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties /msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties

ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.ensemble.common/defaultLog4j.properties "$builderPropertiesFilePath"

ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/mosaicServer.ini /msop/psi_messagereactor/tools/phxMosaicServer/mosaicServer.ini
ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/configuration/config.ini /msop/psi_messagereactor/tools/phxMosaicServer/configuration/config.ini 
ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties /msop/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties


ln -s /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.ensemble.common/defaultLog4j.properties "$serverPropertiesFilePath"


ln -s /msop/pes_rw/psi_messagereactor/tools/scripts/mosaic_request.config /msop/psi_messagereactor/tools/scripts/mosaic_request.config

