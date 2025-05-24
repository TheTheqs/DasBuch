import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { BookDTO } from "../type/BookDTO";
import BookService from "../services/BookService";
import { handleApiError } from "../utils/handleApiError";
import BookForm from "../components/BookForm";
import { useTranslation } from "react-i18next";

function EditBookPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [book, setBook] = useState<BookDTO | null>(null);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const data = await BookService.getBookById(Number(id));
        setBook(data);
      } catch (err) {
        setError(t(handleApiError(err)));
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [id, t]);

  const handleUpdate = async (formData: {
    title: string;
    authors: string[];
  }) => {
    setSuccess("");
    setError("");

    try {
      await BookService.updateBook(Number(id), {
        title: formData.title,
        authors: formData.authors,
      });
      setSuccess(t("editBook.success"));
      navigate(`/book/${id}`);
    } catch (err) {
      setError(t(handleApiError(err)));
    }
  };

  if (loading) return <p className="text-center mt-4">{t("editBook.loading")}</p>;

  if (error || !book)
    return (
      <p className="text-danger text-center mt-4">
        {error || t("editBook.notFound")}
      </p>
    );

  const initialData = {
    title: book.title,
    authors: book.authors.map((a) => a.name),
  };

  return (
    <BookForm
      initialData={initialData}
      onSubmit={handleUpdate}
      submitLabel={t("form.update")}
      success={success}
      error={error}
    />
  );
}

export default EditBookPage;
