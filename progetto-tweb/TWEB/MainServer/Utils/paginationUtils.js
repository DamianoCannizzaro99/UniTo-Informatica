/**
 * Simple pagination utility for common pagination operations
 */
class PaginationUtils {
  
  /**
   * Creates pagination info object
   * @param {number} currentPage - Current page number (1-based)
   * @param {number} pageSize - Number of items per page
   * @param {Array} items - Array of items returned from API
   * @returns {Object} Pagination info object
   */
  static createPaginationInfo(currentPage, pageSize, items) {
    return {
      currentPage: currentPage,
      hasNextPage: items.length === pageSize, // If we got full page, likely more exist
      hasPrevPage: currentPage > 1,
      nextPage: currentPage + 1,
      prevPage: currentPage - 1
    };
  }

  /**
   * Validates pagination parameters
   * @param {number} page - Page number from request
   * @param {number} defaultSize - Default page size
   * @returns {Object} Validated currentPage and pageSize
   */
  static validateParams(page, defaultSize = 20) {
    const currentPage = parseInt(page) || 1;
    const pageSize = defaultSize;
    
    return { currentPage, pageSize };
  }

  /**
   * Converts user page (1-based) to API page (0-based)
   * @param {number} userPage - User-friendly page number
   * @returns {number} API page number
   */
  static toApiPage(userPage) {
    return (parseInt(userPage) || 1) - 1;
  }

  /**
   * Calculates pagination range for displaying page numbers
   * @param {number} currentPage - Current page number
   * @param {number} totalItems - Total number of items (if known)
   * @param {number} pageSize - Items per page
   * @param {number} displayRange - Number of pages to show around current page
   * @returns {Object|null} Object with page numbers to display
   */
  static calculatePageRange(currentPage, totalItems, pageSize, displayRange = 2) {
    // If we don't know total items, estimate based on common patterns
    if (!totalItems) {
      // For homepage, assume at least 5-10 pages of content exist
      // This gives users a better sense of available content
      const estimatedPages = Math.max(5, currentPage + 3);
      return this.buildPageArray(currentPage, estimatedPages, displayRange);
    }
    
    const totalPages = Math.ceil(totalItems / pageSize);
    if (totalPages <= 1) return null;
    
    return this.buildPageArray(currentPage, totalPages, displayRange);
  }

  /**
   * Builds array of page numbers to display
   * @param {number} currentPage - Current page number
   * @param {number} totalPages - Total number of pages
   * @param {number} displayRange - Number of pages to show around current page
   * @returns {Object} Object with page numbers and navigation info
   */
  static buildPageArray(currentPage, totalPages, displayRange) {
    const start = Math.max(1, currentPage - displayRange);
    const end = Math.min(totalPages, currentPage + displayRange);
    
    const pages = [];
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return {
      pages: pages,
      totalPages: totalPages,
      showStartEllipsis: start > 1,
      showEndEllipsis: end < totalPages,
      showFirstPage: start > 1,
      showLastPage: end < totalPages
    };
  }
}

module.exports = PaginationUtils;
