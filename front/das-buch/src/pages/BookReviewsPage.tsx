import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { ReviewDTO } from "../type/ReviewDTO";
import ReviewCard from "../components/ReviewCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import { Spinner } from "react-bootstrap";

function BookReviewsPage() {
  const { id } = useParams<{ id: string }>();
  const [results, setResults] = useState<ReviewDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

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
        setError("Erro ao carregar os reviews." + err);
      } finally {
        setLoading(false);
      }
    };
    fetchReviews();
  }, [id]);

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
      setError("Erro ao mudar de página." + err);
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
    <h3 className="mb-4 text-center fw-bold text-secondary">
      Reviews de Livro
    </h3>

    {results.length === 0 ? (
      <div className="text-center text-muted">Nenhum review encontrado.</div>
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
