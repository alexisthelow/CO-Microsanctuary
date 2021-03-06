package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import data.ActivityDAO;
import data.ItemDAO;
import data.UserDAO;
import entities.Activity;
import entities.Item;
import entities.User;
import helpers.GoogleAddressHelper;

@Controller
public class ItemController {
	
	@Autowired
	ItemDAO itemDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	ActivityDAO activityDAO;
	
	@Autowired
	GoogleAddressHelper gmap;
	
	/**
	 * directs user to search page
	 * @return -- the ModelAndView object
	 */
	@RequestMapping(path = "showSearchPage.do", method = RequestMethod.GET)
	public ModelAndView showSearchPage() {
		return new ModelAndView("searchpage");
	}
	
	/**
	 * shows a list of items filtered by title
	 * @param equipmentType	-- needed to get results
	 * @param equipmentZip  	-- used to get address results
	 * @return				-- the ModelAndView object
	 */
	@RequestMapping(path = "searchResults.do", method = RequestMethod.GET)
	public ModelAndView searchResults(@RequestParam("EquipmentType") String equipmentType, 
			@RequestParam("EquipmentZip") String equipmentZip,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("searchpage");
		User authUser = (User) session.getAttribute("authenticatedUser");
		mv.addObject("authUser", authUser);
		List<Item> searchResults = itemDAO.getItemsByOwnerAddressWithZipCode(equipmentType,equipmentZip);
		mv.addObject("searchResults", searchResults);
		List<String> addresses = new ArrayList<>();
		if (authUser.getPermissionLevel() > 0) {
			for (Item item : searchResults) {
				addresses.add(item.getOwner().getAddress().formatAddress());
			}
			mv.addObject("addresses", addresses);
		}
		return mv;
	}
	
	/**
	 * shows a view with all available items
	 * @param session	-- displays different results based on authenticated user
	 * @return
	 */
	@RequestMapping(path = "showAllItems.do", method = RequestMethod.GET)
	public ModelAndView showAllItems(HttpSession session) {
		ModelAndView mv = new ModelAndView("searchpage");
		User authUser = (User) session.getAttribute("authenticatedUser");
		mv.addObject("authUser", authUser);
		List<Item> searchResults = itemDAO.getAllItems();
		mv.addObject("searchResults", searchResults);
		List<String> addresses = new ArrayList<>();
		if (authUser.getPermissionLevel() > 0) {
			for (Item item : searchResults) {
				String address = item.getOwner().getAddress().formatAddress();
				if (!addresses.contains(address)) {
					addresses.add(address);
				}
			}
			mv.addObject("addresses", addresses);
		}
		return mv;
	}
	
	/**
	 * shows detailed item view, also some user info based on currentUser permission level
	 * if user is permission level 1+, adds item owner info
	 * if user owns item, adds boolean indicating so
	 * @param itemId		-- pass in like so: "itemDetail.do?itemId=${item.id}" 
	 * @param session	-- needed to check user permissions
	 * @return			-- the ModelAndView object
	 */
	@RequestMapping(path = "itemDetail.do", method = RequestMethod.GET)
	public ModelAndView itemDetail(@RequestParam("itemId") int itemId,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("itemDetail");
		Item itemDetail = itemDAO.getItemById(itemId);
		mv.addObject("itemDetail", itemDetail);
		User authUser = (User) session.getAttribute("authenticatedUser");
		
		if (authUser.getPermissionLevel() > 0) {		//they are logged in and can see info
			User itemOwner = userDAO.getUserById(itemDetail.getOwner().getId());
			mv.addObject("itemOwner", itemOwner);
		}
		if (authUser.equals(itemDetail.getOwner())) {		//they're the owner of the item, so they can update it
			mv.addObject("authUserIsItemOwner", true);	//add a boolean to indicate that, so view can create an update link
		}
		return mv;
	}
	
	/**
	 * send user to a page where they can update an item
	 * expect that user is allowed to update item, because they were directed here correctly
	 * @param itemId		-- pass in like so: "showUpdateItem.do?itemId=${item.id}" 
	 * @return			-- the ModelAndView object
	 */
	@RequestMapping(path = "showUpdateItem.do", method = RequestMethod.GET)
	public ModelAndView showUpdateItem(@RequestParam("itemId") int itemId) {
		ModelAndView mv = new ModelAndView("updateItem");
		Item itemToUpdate = itemDAO.getItemById(itemId);
		mv.addObject("itemToUpdate", itemToUpdate);
		return mv;
	}
	
	/**
	 * processes item update and redirects to itemDetail.do
	 * @param updatedItem	-- command item created from spring form in updateItem view
	 * @param redir			-- to add flash attribute for redirect
	 * @return				-- the ModelAndView object
	 */
	@RequestMapping(path = "processItemUpdate.do", method = RequestMethod.POST)
	public ModelAndView processItemUpdate(Item updatedItem, RedirectAttributes redir) {
		ModelAndView mv = new ModelAndView("redirect:itemDetail.do");
		updatedItem = itemDAO.updateItem(updatedItem);
		redir.addFlashAttribute("itemId", updatedItem.getId());
		return mv;
	}
	
	/**
	 * sends user to page where they can add an item
	 * @return
	 */
	@RequestMapping(path = "showAddItem.do", method = RequestMethod.GET)
	public ModelAndView showAddItem() {
		ModelAndView mv = new ModelAndView("addItem");
		Item modelItem = new Item();
		mv.addObject("modelItem", modelItem);
		return mv;
	}
	
	/**
	 * processes add item, redirects to itemDetail.do with item id in flash attributes
	 * @param addedItem	-- command item created from spring form in addItem view
	 * @param session	-- TODO -- needed to set added item's user, unless it's done in spring form
	 * @param redir		-- to add the flash attribute for redirect
	 * @return			-- the ModelAndView object
	 */
	@RequestMapping(path = "processAddItem.do", method = RequestMethod.POST)
	public ModelAndView processAddItem(Item addedItem, HttpSession session) {
		ModelAndView mv = new ModelAndView("itemDetail");
		User authUser = (User) session.getAttribute("authenticatedUser");
		addedItem.setOwner(authUser);
		addedItem = itemDAO.createItem(addedItem);
		mv.addObject("itemDetail", addedItem);
		return mv;
	}
	
	/**
	 * creates an initial borrow request
	 * @param session	-- request is created with user's id
	 * @param id			-- the item to be borrowed
	 * @return
	 */
	@RequestMapping(path = "processRequest.do", method = RequestMethod.GET)
	public ModelAndView processRequest(HttpSession session,
			@RequestParam("itemId") int id) {
		ModelAndView mv = new ModelAndView("userRequest");
		User authUser = (User) session.getAttribute("authenticatedUser");
		Item requestedItem = itemDAO.getItemById(id);
		activityDAO.createItemRequest(requestedItem, authUser);
		List<Activity> userBorrows = activityDAO.viewActivityByUser(authUser);
		List<Activity> userLends = activityDAO.getNewRequestsByUser(authUser);
		mv.addObject("userBorrows", userBorrows);
		mv.addObject("userLends", userLends);
		
		return mv;
	}
	
	/**
	 * confirms that an item has been picked up
	 * @param session	-- only owner can update the record
	 * @param id			-- the id of the request
	 * @return
	 */
	@RequestMapping(path ="processConfirm.do", method = RequestMethod.GET)
	public ModelAndView processConfirm(HttpSession session,
					@RequestParam("activityId") int id) {
		ModelAndView mv = new ModelAndView("userRequest");
		User authUser = (User) session.getAttribute("authenticatedUser");
		Activity getActivity = activityDAO.getActivityById(id);
		activityDAO.confirmLend(getActivity);
		List<Activity> userBorrows = activityDAO.viewActivityByUser(authUser);
		List<Activity> userLends = activityDAO.getNewRequestsByUser(authUser);
		mv.addObject("userBorrows", userBorrows);
		mv.addObject("userLends", userLends);
		
		
		return mv;
		
	}
	
	/**
	 * confirms that an item has been returned
	 * @param session	-- only owner can update record
	 * @param id			-- request id
	 * @return
	 */
	@RequestMapping(path ="processReturn.do", method = RequestMethod.GET)
	public ModelAndView confirmReturn(HttpSession session,
			@RequestParam("activityId") int id) {
		ModelAndView mv = new ModelAndView("userRequest");
		User authUser = (User) session.getAttribute("authenticatedUser");
		Activity getActivity = activityDAO.getActivityById(id);
		activityDAO.confirmReturn(getActivity);
		List<Activity> userBorrows = activityDAO.viewActivityByUser(authUser);
		List<Activity> userLends = activityDAO.getNewRequestsByUser(authUser);
		mv.addObject("userBorrows", userBorrows);
		mv.addObject("userLends", userLends);
		
		
		return mv;
		
	}
	
}
