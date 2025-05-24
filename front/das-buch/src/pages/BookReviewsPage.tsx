import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { ReviewDTO } from "../type/ReviewDTO";
import ReviewCard from "../components/ReviewCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import { Spinner } from "react-bootstrap";
import { useTranslation } from "react-i18next";

function BookReviewsPage() {
  const { id } = useParams<{ id: string }>();
  const [results, setResults] = useState<ReviewDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { t } = useTranslation();

  useEffect(() => {
    const fetchReviews = async () => {
      if (!id) return;
      setLoading(true);
      try {
        const response = await ReviewService.getBookReviews(Number(id), 0, 10);
        setResults(response.content);
        setPageInfo({
          page: response.number,
          totalPages: response.totalPages,
        });
      } catch (err) {
        const translated = t(err as string, { defaultValue: err as string });
        setError(t("bookReviews.loadError") + " " + translated);
      } finally {
        setLoading(false);
      }
    };
    fetchReviews();
  }, [id, t]);

  const handlePageChange = async (newPage: number) => {
    if (!id) return;
    try {
      const response = await ReviewService.getUserReviews(
        Number(id),
        newPage,
        10
      );
      setResults(response.content);
      setPageInfo({
        page: response.number,
        totalPages: response.totalPages,
      });
    } catch (err) {
      const translated = t(err as string, { defaultValue: err as string });
      setError(t("bookReviews.pageChangeError") + " " + translated);
    }
  };

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-5 text-center text-danger">
        {error}
      </div>
    );
  }

  return (
    <div className="container py-4">
      <h3 className="mb-4 text-center fw-bold">
        {t("bookReviews.title")}
      </h3>

      {results.length === 0 ? (
        <div className="text-center text-muted">{t("bookReviews.noResults")}</div>
      ) : (
        <SearchResultGrid>
          {results.map((review) => (
            <ReviewCard key={review.id} review={review} />
          ))}

          <PaginationBar
            currentPage={pageInfo.page}
            totalPages={pageInfo.totalPages}
            onPageChange={handlePageChange}
          />
        </SearchResultGrid>
      )}
    </div>
  );
}

export default BookReviewsPage;
