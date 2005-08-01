#set($webTable=$tag)
#set($tableModel=$webTable.Model)


#if($tableModel)
		<p align="center">
			<table bgcolor="white" border="0" cellpadding="1" cellspacing="0" >
				<tr>
					<td>
						<table  border="0" cellpadding="2" cellspacing="1">

                            <tr bgcolor="yellow">

                            #*
                                Show the visible column names.  Use the display name that can
                                be set in the jsp.
                            *#
                            #foreach($curColumn in $webTable.Columns)
                                #if($curColumn.isVisible())
                                        <th>

                                            #if($webTable.isSortable())
                                                <table border="0" cellspacing="0" cellpadding="0">
												<tr>
												    <td>
												        $curColumn.DisplayName
												    </td>
												    <td>
												        <table border="0" cellspacing="0" cellpadding="0">
												            <tr>
                                                                <td align="bottom">

                                                                    #if(($curColumn.sortColumn == $curColumn.offset) && ($curColumn.sortOrder == 'ASC'))
                                                                        <img src="images/sorted_asc.gif" border="0" align="bottom"/>
                                                                    #else
                                                                        <a href="#bodytag( URL )
                                                                                    #param( "$webTable.sortColumnLinkName $curColumn.offset)
                                                                                    #param( $webTable.sortOrderLinkName 'ASC')
                                                                                 #end>
                                                                       <img src="images/unsorted_asc.gif" border="0" align="bottom"/></a>
                                                                    #end
                                                                </td>
												            </tr>
												            <tr>

                                                                <td align="top">
                                                                    #if(($curColumn.sortColumn == $curColumn.offset) && ($curColumn.sortOrder == 'DESC'))
                                                                        <img src="images/sorted_desc.gif" border="0" align="top"/>
                                                                    #else
                                                                        <a href="#bodytag( URL )
                                                                                    #param( "$webTable.sortColumnLinkName $curColumn.offset)
                                                                                    #param( $webTable.sortOrderLinkName 'DESC')
                                                                                  #end><img src="images/sorted_desc.gif" border="0" align="top"/></a>
                                                                    #end
                                                                </td>
                                                            </tr>
												        </table>

												    </td>
												</tr>
												</table>
                                            #else
                                                $curColumn.DisplayName
                                            #end
                                        </th>
                                #end
                            #end
                            </tr>
                             #foreach($curRow in $webTable.RowIterator)
                                <tr #if($velocityCount % 2 == 1) bgcolor="EEEEFF" #else bgcolor="FFFFFF" #end/>
                                #foreach($curColumn in $curRow)
                                <td>$curColumn</td>

                                #end
                             #end
						</table>
					</td>
				</tr>
			</table>
		</p>

#end
