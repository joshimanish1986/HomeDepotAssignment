package de.hybris.platform.customerreview;

import de.hybris.platform.customerreview.CustomerReviewService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

import java.util.Arrays;
import java.util.List;


public class CustomerReviewUtility {

	// Get the instance of customer service.
	private CustomerReviewService customerReviewService;
	
	// get the curse.banned value from curse.properties file
	@Value("${curse.banned}")
	private String curses;

	
	/*
	 * Method to get a product’s total number of customer reviews whose ratings
	 * are within a given range (inclusive)
	 */

	public Integer getNumberOfReviewsPerRange(ProductModel productModel, int beginRange, int endRange) {
		// Call the service class method to get the review for a product.
		Integer noOfReviews = customerReviewService.getNumberOfReviewsPerRange(productModel, beginRange, endRange);
		return noOfReviews;
	}
	
	
	/*Method to create customer review if
 	a) Customer’s comment contains any of the curse words. If it does, throw an exception with a message.
	b) Check if the rating is not < 0.  If it is < 0, throw an exception with a message.
	*/

	public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user,
			ProductModel product) {
		// check if there are no curse in the comment.
		CustomerReviewModel customerReviewModel = null;
		if (null != comment && hasNoCurseWords(comment)) {
			//check if rating is less than 0.
			if (rating >= 0) {
			// call the service method to create the customer review.
				customerReviewModel= customerReviewService.createCustomerReview(rating, headline, comment, user, product);
			}
		// throw an exception because rating is less than 0.
			else {
				throw new RuntimeException("The review cannot be created because rating is below 0");
			}

		}
		// throw an exception because comment has curse words.
		else {
			throw new RuntimeException("The review cannot be created because comment has curse words.");
		}
		return customerReviewModel;
	}
	
	// Method to check if the comment has any curse words.
	public boolean hasNoCurseWords(String comment) {

		boolean isCurseFree = true;
		//split the curses string and create a String[].
		String[] curse = curses.split(",");
		//convert the String[] to List of curse strings.
		List<String> curseList = Arrays.asList(curse);
		if (null != curseList && !curseList.isEmpty()) {
			//Check if the comment contains any of the curse in curseList
			for (String curseValue : curseList) {

				if (comment.contains(curseValue)) {

					isCurseFree = false;
					break;
				}
			}

		}

		return isCurseFree;

	}

	public CustomerReviewService getCustomerReviewService() {
		return customerReviewService;
	}

	
	public void setCustomerReviewService(CustomerReviewService customerReviewService) {
		this.customerReviewService = customerReviewService;
	}


	public String getCurses() {
		return curses;
	}

	public void setCurses(String curses) {
		this.curses = curses;
	}

}
