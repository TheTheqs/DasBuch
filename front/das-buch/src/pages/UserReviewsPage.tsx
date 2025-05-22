import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { ReviewDTO } from "../type/ReviewDTO";
import ReviewCard from "../components/ReviewCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import UserProfile from "../components/UserProfile";
import { Spinner } from "react-bootstrap";
import UserService from "../services/UserService";
import { UserDTO } from "../type/UserDTO";
import { useTranslation } from "react-i18next";

function UserReviewsPage() {
  const { id } = useParams<{ id: string }>();
  const [results, setResults] = useState<ReviewDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [user, setUser] = useState<UserDTO | null>(null);
  const { t } = useTranslation();

  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;
      setLoading(true);
      try {
        const [reviewResponse, userResponse] = await Promise.all([
          ReviewService.getUserReviews(Number(id), 0, 10),
          UserService.getUserById(Number(id)),
        ]);

        setResults(reviewResponse.content);
        setPageInfo({
          page: reviewResponse.number,
          totalPages: reviewResponse.totalPages,
        });
        setUser(userResponse);
      } catch (err) {
        setError(t("userReviews.fetchError") + " " + err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
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
      setError(t("userReviews.paginationError") + " " + err);
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
      <div className="container py-5 text-center text-danger">{error}</div>
    );
  }

  return (
    <div className="container py-4">
      {user && <UserProfile relatedUser={user} />}
      <h3 className="mb-4 text-center fw-bold">{t("userReviews.title")}</h3>

      {results.length === 0 ? (
        <div className="text-center text-muted">
          {t("userReviews.noResults")}
        </div>
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

export default UserReviewsPage;
