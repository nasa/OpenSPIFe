#!/bin/bash

rm -rf /msop/pes_rw/psi

mkdir -p /msop/pes_rw/psi/
mkdir -p /msop/pes_rw/psi/configuration/
mkdir -p /msop/pes_rw/psi/plugins/gov.nasa.ensemble.common/
mkdir -p /msop/pes_rw/psi/plugins/gov.nasa.ensemble.core.jms/

# MESSAGE REACTOR STUFF
rm -rf /msop/pes_rw/psi_messagereactor
mkdir -p /msop/pes_rw/psi_messagereactor/logs/pipeline
mkdir -p /msop/pes_rw/psi_messagereactor/conf/
mkdir -p /msop/pes_rw/psi_messagereactor/catalogPopulator/
mkdir -p /msop/pes_rw/psi_messagereactor/harmonic/
mkdir -p /msop/pes_rw/psi_messagereactor/phxMosaicBuilder
mkdir -p /msop/pes_rw/psi_messagereactor/phxMosaicServer
mkdir -p /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/configuration/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.ensemble.common/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/harmonic
mkdir -p /msop/pes_rw/psi_messagereactor/tools/harmonic/configuration
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/configuration
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.ensemble.common/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/configuration/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.ensemble.common/
mkdir -p /msop/pes_rw/psi_messagereactor/tools/scripts


ensemblePropertiesDirectory=`ls -t /msop/psi/plugins/ | grep "gov\.nasa\.ensemble\.phoenix\.rcp_.*" | tail -1`
ensemblePropertiesFilePath="/msop/psi/plugins/${ensemblePropertiesDirectory}/ensemble.properties"	

cp "$ensemblePropertiesFilePath" /msop/pes_rw/psi

cp /msop/psi_messagereactor/conf/config.pl /msop/pes_rw/psi_messagereactor/conf/
cp /msop/psi_messagereactor/conf/PSI_MessageReactor.xml /msop/pes_rw/psi_messagereactor/conf/
cp /msop/psi_messagereactor/tools/do-pipeline.config /msop/pes_rw/psi_messagereactor/tools/
cp /msop/psi_messagereactor/tools/catalogPopulator/catalogPopulator.ini /msop/pes_rw/psi_messagereactor/tools/catalogPopulator
cp /msop/psi_messagereactor/tools/catalogPopulator/configuration/config.ini /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/configuration
cp /msop/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/ensemble.properties /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/


catPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/catalogPopulator/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
catPropertiesFilePath="/msop/psi_messagereactor/tools/catalogPopulator/plugins/${catPropertiesDirectory}/defaultLog4j.properties"	
cp "$catPropertiesFilePath" /msop/pes_rw/psi_messagereactor/tools/catalogPopulator/plugins/gov.nasa.ensemble.common/

cp /msop/psi_messagereactor/tools/harmonic/harmonic.ini /msop/pes_rw/psi_messagereactor/tools/harmonic
cp /msop/psi_messagereactor/tools/harmonic/configuration/config.ini /msop/pes_rw/psi_messagereactor/tools/harmonic/configuration
cp /msop/psi_messagereactor/tools/harmonic/dir-wrap-harmonic /msop/pes_rw/psi_messagereactor/tools/harmonic
cp /msop/psi_messagereactor/tools/phxMosaicBuilder/mosaicker.ini /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/
cp /msop/psi_messagereactor/tools/phxMosaicBuilder/configuration/config.ini /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/configuration
cp /msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/

builderPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
builderPropertiesFilePath="/msop/psi_messagereactor/tools/phxMosaicBuilder/plugins/${builderPropertiesDirectory}/defaultLog4j.properties"	
cp "$builderPropertiesFilePath" /msop/pes_rw/psi_messagereactor/tools/phxMosaicBuilder/plugins/gov.nasa.ensemble.common/

cp /msop/psi_messagereactor/tools/phxMosaicServer/mosaicServer.ini /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/
cp /msop/psi_messagereactor/tools/phxMosaicServer/configuration/config.ini /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/configuration/
cp /msop/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/ensemble.properties /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.jpl.orchestrator.phoenix_1.0.0/

serverPropertiesDirectory=`ls -t /msop/psi_messagereactor/tools/phxMosaicServer/plugins/ | grep "gov\.nasa\.ensemble\.core\.common_.*" | tail -1`
serverPropertiesFilePath="/msop/psi_messagereactor/tools/phxMosaicServer/plugins/${serverPropertiesDirectory}/defaultLog4j.properties"	
cp "$serverPropertiesFilePath" /msop/pes_rw/psi_messagereactor/tools/phxMosaicServer/plugins/gov.nasa.ensemble.common/

cp /msop/psi_messagereactor/tools/scripts/mosaic_request.config /msop/pes_rw/psi_messagereactor/tools/scripts


cp /msop/psi/PSI.ini  /msop/pes_rw/psi/
cp /msop/psi/configuration/config.ini /msop/pes_rw/psi/configuration/
cp /msop/psi/plugins/gov.nasa.ensemble.common_1.0.15.200708171557/defaultLog4j.properties /msop/pes_rw/psi/plugins/gov.nasa.ensemble.common/
#cp /msop/psi/plugins/gov.nasa.ensemble.core.jms_1.0.9.200705161814/activemq_PSI.properties /msop/pes_rw/psi/plugins/gov.nasa.ensemble.core.jms/
cp /msop/psi_messagereactor/conf/config.pl /msop/pes_rw/psi_messagereactor/conf/
cp /msop/psi_messagereactor/conf/PSI_MessageReactor.xml /msop/pes_rw/psi_messagereactor/conf/
#cp /msop/psi_messagereactor/catalog/CatalogPopulator/configuration/config.ini /msop/pes_rw/psi_messagereactor/catalog/CatalogPopulator/configuration/
#cp /msop/psi_messagereactor/catalog/CatalogPopulator/catalogPopulator.ini /msop/pes_rw/psi_messagereactor/catalog/CatalogPopulator/
#cp /msop/psi_messagereactor/catalog/CatalogPopulator/plugins/gov.nasa.ensemble.common_1.0.15.200705171101/defaultLog4j.properties  /msop/pes_rw/psi_messagereactor/catalog/CatalogPopulator/plugins/gov.nasa.ensemble.common/
#cp /msop/psi_messagereactor/catalog/CatalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config_1.0.0/ensemble.properties /msop/pes_rw/psi_messagereactor/catalog/CatalogPopulator/plugins/gov.nasa.jpl.maestro.phoenix.catalog.config/


rm -rf /msop/pes_rw/activemq
mkdir -p /msop/pes_rw/activemq