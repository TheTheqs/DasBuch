import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { BookDTO } from "../type/BookDTO";
import BookService from "../services/BookService";
import { handleApiError } from "../utils/handleApiError";
import BookForm from "../components/BookForm";

function EditBookPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

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
        setError(handleApiError(err));
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [id]);

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
      setSuccess("Livro atualizado com sucesso!");
      navigate(`/book/${id}`);
    } catch (err) {
      setError(handleApiError(err));
    }
  };

  if (loading) return <p className="text-center mt-4">Carregando livro...</p>;
  if (error || !book)
    return (
      <p className="text-danger text-center mt-4">
        {error || "Livro não encontrado."}
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
      submitLabel="Atualizar"
      success={success}
      error={error}
    />
  );
}

export default EditBookPage;
