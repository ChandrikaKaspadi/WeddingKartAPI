package weddingKart_Endpoints;

public interface IEndPoints {
	//User Login Auth
	public String authenticate ="/auth/";
	public String sendOTP="/auth";
	public String updateUserDetails="/auth/update_user_details";
	public String userCredits="/get_user_credits";
	
	
	//Wedding CRUD
	public String weddingsAllWeddingList="/weddings/list_all_weddings";
	public String addNewWedding="/weddings/";
	public String weddingsUpdateWeddingDetails="/weddings/update_wedding_details";
	public String singleWedding="/weddings";
	public String deleteWedding="/weddings/";
	public String weddingsListOfWeddingCreatedByMe="/weddings/list_weddings_created_by_me";
	
	//Groups CRUD
	
	public String groupsListGroupsInWedding="/groups/list_groups";
	public String groupsAddNewGroup="/groups/add_new_group";
	public String groupsDeleteGroupFromWedding="/groups/delete_group_by_id_or_name";
	public String groupsDeleteAllGroups="/groups/delete_all_groups";
	public String groupsMergeGroup="/groups/merge_groups";
	public String groupsRenameGroup="/groups/rename_group";
	
	//Guest CRUD
	
	public String guestsAddNewGuest="/guests/add_new_guest";
	public String guestsMoveGuestToGroup="/guests/move_to_group";
	public String guestsCopyGuestToGroup="/guests/copy_to_group";
	public String guestsDeleteGuestFromGroup="/guests/delete_from_group";
	public String guestsUpdateGuest="/guests/update_guest";
	public String guestsGetGuestByID="/guests/get_guest_by_id";
	public String guestsListAllGuest="/v2/guests/list_all_guests";
	public String guestsPurgeGuests="/guests/purge_guests";
	public String guestsPurgeAllGuests="/guests/purge_all_guests";
	public String guestsParseGuestExcel="/guests/parse_guest_excel";
	
	//Event CRUD
	
	public String eventsAddEvent="/events/add_new_event";
	public String eventsListEvents="/events/list_events";
	public String eventsUpdateEvent="/events/update_event";
	public String eventsGetEventById="/events/get_event_by_id";
	public String eventsDeleteEventById="/events/delete_event_by_id";
	
	
	
	
}
