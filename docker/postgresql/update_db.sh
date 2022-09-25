echo "################################################################"
echo "## Update BD                                                  ##"
echo "## -----------------------------------------------------------##"
echo "## with the command below:                                    ##"
echo "##                                                            ##"
echo "## 'docker exec -it local_pg psql -U postgres -d mh_crud'     ##"
echo "##                                                            ##"
echo "##                                                            ##"
echo "## Obs.:                                                      ##"
echo "##       - all commands must end with ;                       ##"
echo "##       - to exit press '\q'                                 ##"
echo "################################################################"
echo
echo "Opening the db in 5 seconds..."
sleep 5

docker exec -it local_pg psql -U postgres -d mh_crud

