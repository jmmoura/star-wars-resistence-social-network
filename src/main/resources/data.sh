#!/bin/bash

psql -U postgres -d star-wars

INSERT INTO sw_user(password, role, username) VALUES ('$2a$10$NWnDpD7cwlV7CEDfH1KWEOkMDO8MS.vchId1UhR7GT7vYVpI9tUfW', 'admin', 'josielADM');
INSERT INTO sw_user(password, role, username) VALUES ('$2a$10$NWnDpD7cwlV7CEDfH1KWEOkMDO8MS.vchId1UhR7GT7vYVpI9tUfW', 'rebel', 'josiel');

SELECT * FROM sw_user;

\q
