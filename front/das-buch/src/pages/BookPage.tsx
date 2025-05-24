import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import BookService from "../services/BookService";
import { BookDTO } from "../type/BookDTO";
import { Spinner } from "react-bootstrap";
import PrimaryButton from "../components/PrimaryButton";
import { useTranslation } from "react-i18next";

function BookPage() {
  const { id } = useParams<{ id: string }>();
  const [book, setBook] = useState<BookDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { t } = useTranslation();

  useEffect(() => {
    const fetchBook = async () => {
      if (!id) return;
      setLoading(true);
      try {
        const data = await BookService.getBookById(Number(id));
        setBook(data);
      } catch (err) {
        const translated = t(err as string, { defaultValue: err as string });
        setError(t("bookPage.loadError") + " " + translated);
      } finally {
        setLoading(false);
      }
    };
    fetchBook();
  }, [id, t]);

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  if (error || !book) {
    return (
      <div className="container py-5 text-center text-danger">
        {error || t("bookPage.notFound")}
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="bg-light p-4 rounded shadow-sm mb-2">
        <div className="mb-2 text-muted small">
          {t("bookPage.idLabel", { id: book.id })}
        </div>
        <h2 className="fw-bold mb-0">{book.title}</h2>

        <div className="mb-3">
          <strong>{t("bookPage.authors")}</strong>{" "}
          {book.authors.length > 0
            ? book.authors.map((a) => a.name).join(", ")
            : t("bookPage.unknownAuthor")}
        </div>

        <div className="mb-3">
          <strong>{t("bookPage.reviewCount")}</strong> {book.reviewCount}
        </div>
      </div>

      <div className="d-flex gap-2">
        <PrimaryButton to={`/book/reviews/${book.id}`} label={t("bookPage.viewReviews")} />
        <PrimaryButton to={`/new/${book.id}`} label={t("bookPage.createReview")} />
      </div>
    </div>
  );
}

export default BookPage;
